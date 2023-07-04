package com.latmn.kangaroo.platform.gateway.repository;


import com.latmn.kangaroo.platform.gateway.model.po.DbRoutePo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface DbRouteRepository {
    Flux<DbRoutePo> findAll();

    /**
     * 白名单
     */
    Mono<Long> countWL(String requestPath);
}
