package com.latmn.kangaroo.platform.news.filter;

import com.latmn.kangaroo.framework.core.define.Define;
import com.latmn.kangaroo.framework.core.domain.UserDomain;
import com.latmn.kangaroo.platform.news.repository.DbRouteRepository;
import com.latmn.kangaroo.platform.news.repository.RBACUserRepository;
import com.latmn.kangaroo.platform.news.util.PathUtil;
import com.latmn.kangaroo.platform.news.util.WriteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

import static org.springframework.cloud.gateway.filter.NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER;

@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private final ReactiveRedisOperations<String, Object> reactiveRedisOperations;
    private final RBACUserRepository rbacUserRepository;

    private final DbRouteRepository dbRouteRepository;


    public AuthFilter(ReactiveRedisOperations<String, Object> reactiveRedisOperations,
                      RBACUserRepository rbacUserRepository,
                      DbRouteRepository dbRouteRepository) {
        this.reactiveRedisOperations = reactiveRedisOperations;
        this.rbacUserRepository = rbacUserRepository;
        this.dbRouteRepository = dbRouteRepository;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        return dbRouteRepository.findAllWl()
                .flatMap(wl -> {
                    //白名单
                    if (PathUtil.pathMatch(request.getPath().toString(), wl)) {
                        return chain.filter(exchange);
                    } else {
                        //获取头部信息
                        HttpHeaders headers = request.getHeaders();
                        List<String> authTokenList = headers.get(Define.AUTH_HEADER_NAME);
                        String requestId = request.getHeaders().get(Define.TRACE_ID).get(0);
                        if (CollectionUtils.isEmpty(authTokenList)) {
                            logger.info(Define.AUTH_ERROR_MESSAGE);
                            return WriteUtil.error(exchange, WriteUtil.authFailResult(Define.AUTH_ERROR_CODE, Define.AUTH_ERROR_MESSAGE, requestId, request.getPath().toString()), HttpStatus.FORBIDDEN);
                        }
                        String apiKey = authTokenList.get(0);
                        if (!StringUtils.hasText(apiKey)) {
                            logger.info(Define.AUTH_ERROR_MESSAGE);
                            return WriteUtil.error(exchange, WriteUtil.authFailResult(Define.AUTH_ERROR_CODE, Define.AUTH_ERROR_MESSAGE, requestId, request.getPath().toString()), HttpStatus.FORBIDDEN);
                        }
                        //验证redis
                        return reactiveRedisOperations.opsForValue().get(Define.CACHE_USER_PREFIX + apiKey)
                                .switchIfEmpty(Mono.defer(() -> Mono.just(Define.EMPTY_STR)))
                                .flatMap(it -> {
                                    if (null != it) {
                                        //检查状态
                                        UserDomain userDomain = (UserDomain) it;
                                        if (null == userDomain || !StringUtils.hasText(userDomain.getUserCode()) || !StringUtils.hasText(userDomain.getId())) {
                                            return WriteUtil.error(exchange, WriteUtil.authFailResult(Define.AUTH_ERROR_CODE, Define.AUTH_ERROR_MESSAGE, requestId, request.getPath().toString()), HttpStatus.FORBIDDEN);
                                        }
                                        return rbacUserRepository.findUser(userDomain.getId())
                                                .defaultIfEmpty(UserDomain.builder().id("-1").build())
                                                .flatMap(user -> {
                                                    if ("-1".equalsIgnoreCase(user.getId())) {
                                                        logger.info(Define.AUTH_TOKEN_ABSENT + ": {}", userDomain);
                                                        return WriteUtil.error(exchange, WriteUtil.authFailResult(Define.AUTH_ERROR_CODE, Define.AUTH_TOKEN_ABSENT, requestId, request.getPath().toString()), HttpStatus.FORBIDDEN);
                                                    } else {
                                                        Integer userState = user.getUserState();
                                                        if (null == userState) {
                                                            logger.info("用户状态配置不正确: {}", userDomain);
                                                            return WriteUtil.error(exchange, WriteUtil.authFailResult(Define.AUTH_ERROR_CODE, Define.AUTH_TOKEN_ABSENT, requestId, request.getPath().toString()), HttpStatus.FORBIDDEN);
                                                        } else {
                                                            if (1 == userState) {
                                                                reactiveRedisOperations.expire(apiKey, Duration.ofDays(30)).subscribe();
                                                                //url鉴权
                                                                return rbacUserRepository.findAllUserUri(userDomain.getId())
                                                                        .flatMap(userUri -> {
                                                                            boolean flag = PathUtil.pathMatch(request.getPath().toString(), userUri);
                                                                            if (flag) {
                                                                                return chain.filter(exchange);
                                                                            } else {
                                                                                logger.info("无权限访问! user: {}  path: {}", userDomain, request.getPath());
                                                                                return WriteUtil.error(exchange, WriteUtil.authFailResult(Define.AUTH_ERROR_CODE, Define.PATH_ACCESS_NOT_POWER, requestId, request.getPath().toString()), HttpStatus.FORBIDDEN);
                                                                            }
                                                                        });
                                                            } else {
                                                                logger.info("用户状态 != 1: {}", userDomain);
                                                                return WriteUtil.error(exchange, WriteUtil.authFailResult(Define.AUTH_ERROR_CODE, Define.AUTH_TOKEN_ABSENT, requestId, request.getPath().toString()), HttpStatus.FORBIDDEN);
                                                            }
                                                        }
                                                    }
                                                });
                                    } else {
                                        logger.info("认证失败: apiKey 无效!");
                                        return WriteUtil.error(exchange, WriteUtil.authFailResult(Define.AUTH_ERROR_CODE, Define.AUTH_ERROR_MESSAGE, requestId, request.getPath().toString()), HttpStatus.FORBIDDEN);
                                    }
                                });
                    }
                });
    }


    @Override
    public int getOrder() {
        return WRITE_RESPONSE_FILTER_ORDER - 99;
    }
}
