package com.latmn.kangaroo.platform.news.config;

import com.latmn.kangaroo.platform.news.repository.DbRouteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

@Configuration
public class RouteConfig {

    private final Logger logger = LoggerFactory.getLogger(RouteConfig.class);


    private final DbRouteRepository dbRouteRepository;
    private final RouteLocatorBuilder routeLocatorBuilder;

    public RouteConfig(DbRouteRepository dbRouteRepository, RouteLocatorBuilder routeLocatorBuilder) {
        this.dbRouteRepository = dbRouteRepository;
        this.routeLocatorBuilder = routeLocatorBuilder;
    }

    @Bean
    public RouteLocator DbRouteLocator() {
        return () -> Flux.just(routeLocatorBuilder.routes())
                .flatMap(routes -> dbRouteRepository.findAll()
                        .filter(fr -> StringUtils.hasText(fr.getUri()) || StringUtils.hasText(fr.getId()) || StringUtils.hasText(fr.getPath()))
                        .map(pr -> routes.route(pr.getId(),
                                predicateSpec -> predicateSpec.path(handlerPath(pr.getPath())).and().method(
                                                HttpMethod.GET,
                                                HttpMethod.POST,
                                                HttpMethod.PUT,
                                                HttpMethod.DELETE,
                                                HttpMethod.PATCH,
                                                HttpMethod.HEAD,
                                                HttpMethod.OPTIONS,
                                                HttpMethod.TRACE
                                        ).filters(
                                                f -> f.rewritePath(pr.getPath() + "(?<segment>.*)", pr.getPath() + "${segment}")
                                        )
                                        .uri(pr.getUri())))
                        .collectList()
                        .flatMapMany(builders -> routes.build().getRoutes()))
                .doOnNext(pr -> logger.info("db route (add or reload): {}", pr));
    }

    private String handlerPath(String path) {
        if (!path.endsWith("/**")) {
            return path + "/**";
        }
        return path;
    }
}
