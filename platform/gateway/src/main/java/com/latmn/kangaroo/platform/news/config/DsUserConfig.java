package com.latmn.kangaroo.platform.news.config;


import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
@EnableR2dbcRepositories(entityOperationsRef = "dsUserEntityTemplate")
public class DsUserConfig {

    @Value("${ds.user.uri}")
    private String dsUserUri;

    @Bean()
    @Qualifier("dsUserConnectionFactory")
    public ConnectionFactory dsUserConnectionFactory() {
        return ConnectionFactories.get(dsUserUri);
    }


    @Bean
    public R2dbcEntityOperations dsUserEntityTemplate(@Qualifier("dsUserConnectionFactory") ConnectionFactory connectionFactory) {
        DefaultReactiveDataAccessStrategy strategy = new DefaultReactiveDataAccessStrategy(MySqlDialect.INSTANCE);
        DatabaseClient databaseClient = DatabaseClient.builder()
                .connectionFactory(connectionFactory)
                .bindMarkers(MySqlDialect.INSTANCE.getBindMarkersFactory())
                .build();
        return new R2dbcEntityTemplate(databaseClient, strategy);
    }


    /*@PostConstruct
    public void initialize() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScripts(
                new ClassPathResource("scripts/orders/schema.sql"),
                new ClassPathResource("scripts/orders/data.sql")
        );
        databasePopulator.populate(ordersConnectionFactory()).subscribe();
    }*/
}
