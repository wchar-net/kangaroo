package com.latmn.kangaroo.platform.gateway.repository.impl;

import com.latmn.kangaroo.framework.core.domain.UserDomain;
import com.latmn.kangaroo.platform.gateway.repository.RBACUserRepository;
import dev.miku.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.BiFunction;

@Service
public class RBACUserRepositoryImpl implements RBACUserRepository {


    private final R2dbcEntityOperations dsUserEntityTemplate;
    private final ConnectionFactory connectionFactory;

    public RBACUserRepositoryImpl(@Qualifier("dsUserEntityTemplate") R2dbcEntityOperations dsUserEntityTemplate,
                                  @Qualifier("dsUserConnectionFactory") ConnectionFactory connectionFactory) {
        this.dsUserEntityTemplate = dsUserEntityTemplate;
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Mono<List<String>> findAllUserUri(String userId) {
        String sql = """
                SELECT
                	rp.power_uri as powerUri
                FROM
                	rbac_role role,
                	rbac_role_power power,
                	rbac_power rp
                WHERE
                	role.del_flag = 1
                	AND power.del_flag = 1
                	AND power.role_id = role.id
                	AND rp.id = power.power_id
                	AND rp.del_flag = 1
                	AND role.id IN (
                	SELECT
                		r.role_id
                	FROM
                		rbac_user u,
                		rbac_user_role r
                	WHERE
                		u.del_flag = 1
                		AND u.id = ?
                		AND r.del_flag = '1'
                	AND r.user_id = u.id
                	)
                """;
        MySqlConnectionFactory mySqlConnectionFactory = (MySqlConnectionFactory) connectionFactory;
        Mono<Connection> connection = Mono.from(mySqlConnectionFactory.create());
        return connection.flatMapMany(conn -> conn.createStatement(sql).bind(0, userId).execute())
                .flatMap(result -> result.map((row, meta) -> {
                    String uri = (String) row.get("powerUri");
                    return uri;
                })).collectList();
    }
    @Override
    public Mono<UserDomain> findUser(String userId) {
        /*  todo r2dbc mysql drive bind bug
        return dsUserEntityTemplate.getDatabaseClient()
                .sql("SELECT r.user_state as userState,r.id,r.user_code as userCode from rbac_user r where r.del_flag = :delFlag and r.id = :id")
                .bind("delFlag", 1)
                .bind("id", userId)
                .map(mappingSimpleRbacUser)
                .one();*/

        String sql = "SELECT r.user_state as userState,r.id,r.user_code as userCode from rbac_user r where r.del_flag = ? and r.id = ?";
        MySqlConnectionFactory mySqlConnectionFactory = (MySqlConnectionFactory) connectionFactory;
        Mono<Connection> connection = Mono.from(mySqlConnectionFactory.create());

        BiFunction<Row, RowMetadata, UserDomain> mappingSimpleRbacUser = (row, rowMetaData) -> UserDomain.builder()
                .id(row.get("id", String.class))
                .userCode(row.get("userCode", String.class))
                .userState(row.get("userState", Integer.class))
                .build();
        return connection.flatMapMany(conn -> conn.createStatement(sql).bind(0, 1).bind(1, userId).execute())
                .flatMap(result -> result.map(mappingSimpleRbacUser)).take(1).next();
    }
}
