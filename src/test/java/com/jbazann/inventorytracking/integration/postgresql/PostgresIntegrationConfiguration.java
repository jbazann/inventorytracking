package com.jbazann.inventorytracking.integration.postgresql;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;


@Configuration
@ComponentScan("com.jbazann.inventorytracking")
@PropertySource(value = "classpath:application.properties")
public class PostgresIntegrationConfiguration {

    @Value("${postgresimage.repo}")
    private String imageRepo;
    @Value("${postgresimage.tag}")
    private String imageTag;
    @Value("${postgres.database.name}")
    private String dbname;

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgresqlContainer() {
        try(PostgreSQLContainer<?> container = new PostgreSQLContainer<>(
                DockerImageName.parse(imageRepo+':'+imageTag)
                        .asCompatibleSubstituteFor("postgres")
        )) {
            return container.withDatabaseName(dbname);
        }
    }

    @Bean
    public PostgresIntegrationTestData integrationTestData() {
        return new PostgresIntegrationTestData();
    }

}
