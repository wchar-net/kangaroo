package com.latmn.kangaroo.platform.news.repository.impl;

import com.latmn.kangaroo.platform.news.model.po.DbRoutePo;
import com.latmn.kangaroo.platform.news.repository.DbRouteRepository;
import dev.miku.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DbRouteRepositoryImpl implements DbRouteRepository {

    private final R2dbcEntityOperations dsGatewayEntityTemplate;

    private final ConnectionFactory connectionFactory;

    public DbRouteRepositoryImpl(@Qualifier("dsGatewayEntityTemplate") R2dbcEntityOperations dsGatewayEntityTemplate,
                                 @Qualifier("dsGatewayConnectionFactory") ConnectionFactory connectionFactory) {
        this.dsGatewayEntityTemplate = dsGatewayEntityTemplate;
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Flux<DbRoutePo> findAll() {
        return dsGatewayEntityTemplate.select(Query.query(Criteria.where("del_flag").is(1)), DbRoutePo.class);
    }

    @Override
    public Mono<List<String>> findAllWl() {
        String sql = "SELECT w.url from wl w where w.del_flag = ?";
        MySqlConnectionFactory mySqlConnectionFactory = (MySqlConnectionFactory) connectionFactory;
        Mono<Connection> connection = Mono.from(mySqlConnectionFactory.create());
        return connection.flatMapMany(conn -> conn.createStatement(sql).bind(0, 1).execute())
                .flatMap(result -> result.map((row, meta) -> {
                    String uri = (String) row.get("url");
                    return uri;
                })).collectList();
    }
}
