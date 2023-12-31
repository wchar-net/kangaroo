package com.latmn.kangaroo.platform.gateway.repository;


import com.latmn.kangaroo.platform.gateway.model.po.DbRoutePo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public interface DbRouteRepository {
    Flux<DbRoutePo> findAll();


    /**
     * 获取所有白名单
     */
    Mono<List<String>> findAllWl();
}
