package com.latmn.kangaroo.platform.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.latmn.kangaroo.framework.core.define.Define;
import com.latmn.kangaroo.framework.core.domain.UserDomain;
import com.latmn.kangaroo.platform.gateway.repository.DbRouteRepository;
import com.latmn.kangaroo.platform.gateway.repository.RBACUserRepository;
import com.latmn.kangaroo.platform.gateway.util.WriteUtil;
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
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static org.springframework.cloud.gateway.filter.NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER;

@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private final ReactiveRedisOperations<String, String> reactiveRedisOperations;
    private final RBACUserRepository rbacUserRepository;

    private final DbRouteRepository dbRouteRepository;

    private final ObjectMapper objectMapper;

    public AuthFilter(ReactiveRedisOperations<String, String> reactiveRedisOperations, RBACUserRepository rbacUserRepository, DbRouteRepository dbRouteRepository, ObjectMapper objectMapper) {
        this.reactiveRedisOperations = reactiveRedisOperations;
        this.rbacUserRepository = rbacUserRepository;
        this.dbRouteRepository = dbRouteRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //静态资源
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean matchWebJars = antPathMatcher.match("/**/webjars/**", request.getPath().toString());
        boolean matchSwaggerConfig = antPathMatcher.match("/**/v3/api-docs/**", request.getPath().toString());
        if (matchWebJars || matchSwaggerConfig) {
            return chain.filter(exchange);
        }
        //白名单
        return dbRouteRepository.countWL(request.getPath().toString())
                .flatMap(count -> {
                    if (count > 0) {
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
                        return reactiveRedisOperations.opsForValue().get(apiKey)
                                .switchIfEmpty(Mono.defer(() -> Mono.just(Define.EMPTY_STR)))
                                .flatMap(it -> {
                                    String userJsonStr = String.valueOf(it);
                                    if (StringUtils.hasText(userJsonStr)) {
                                        //检查状态
                                        UserDomain userDomain;
                                        try {
                                            userDomain = objectMapper.readValue(userJsonStr, UserDomain.class);
                                        } catch (IOException e) {
                                            logger.info("redis value 解析失败 非法用户信息!");
                                            return WriteUtil.error(exchange, WriteUtil.authFailResult(Define.AUTH_ERROR_CODE, Define.AUTH_TOKEN_ILLEGAL, requestId, request.getPath().toString()), HttpStatus.FORBIDDEN);
                                        }
                                        if (null == userDomain || !StringUtils.hasText(userDomain.getUserCode()) || !StringUtils.hasText(userDomain.getId())) {
                                            return WriteUtil.error(exchange, WriteUtil.authFailResult(Define.AUTH_ERROR_CODE, Define.AUTH_ERROR_MESSAGE, requestId, request.getPath().toString()), HttpStatus.FORBIDDEN);
                                        }
                                        return rbacUserRepository.findUser(userDomain.getId())
                                                .defaultIfEmpty(UserDomain.builder().id("-1").build())
                                                .flatMap(user -> {
                                                    if ("-1" .equalsIgnoreCase(user.getId())) {
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
                                                                return chain.filter(exchange);
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
