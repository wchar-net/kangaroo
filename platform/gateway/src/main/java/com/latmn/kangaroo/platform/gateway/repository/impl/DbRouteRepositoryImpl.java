package com.latmn.kangaroo.platform.gateway.repository.impl;

import com.latmn.kangaroo.framework.core.domain.UserDomain;
import com.latmn.kangaroo.platform.gateway.model.po.DbRoutePo;
import com.latmn.kangaroo.platform.gateway.repository.DbRouteRepository;
import dev.miku.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

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
    public Mono<Long> countWL(String requestPath) {
        String sql = "SELECT count(1) from wl w where w.del_flag = 1 and w.url = ?";
        MySqlConnectionFactory mySqlConnectionFactory = (MySqlConnectionFactory) connectionFactory;
        Mono<Connection> connection = Mono.from(mySqlConnectionFactory.create());
        return connection.flatMapMany(conn -> conn.createStatement(sql).bind(0, requestPath).execute())
                .flatMap(result -> result.map((row, meta) -> {
                    Long count = (Long) row.get(0);
                    return count;
                })).take(1).next();
    }
}
