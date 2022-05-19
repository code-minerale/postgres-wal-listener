package com.fiko.cdc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The configurations required to setup a Debezium connector.
 *
 * @author Fiko
 */
@Configuration
public class DebeziumConnectorConfig {

    /**
     *  Database details.
     */
    @Value("${datasource.host}")
    private String databaseHost;

    @Value("${datasource.databasename}")
    private String databaseName;

    @Value("${datasource.port}")
    private String databasePort;

    @Value("${datasource.username}")
    private String databaseUser;

    @Value("${datasource.password}")
    private String databasePassword;

    private String TEST_TABLE_NAME = "public.test_table";

    /**
     * db connector.
     *
     * @return Configuration.
     */
    @Bean
    public io.debezium.config.Configuration testConnector() {
        return io.debezium.config.Configuration.create()
                .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
                .with("offset.storage",  "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", "/Users/zulfikri/Downloads/test-offset.dat")
                .with("offset.flush.interval.ms", 60000)
                .with("name", "test-postgres-connector")
                .with("database.server.name", databaseHost+"-"+databaseName)
                .with("database.hostname", databaseHost)
                .with("database.port", databasePort)
                .with("database.user", databaseUser)
                .with("database.password", databasePassword)
                .with("database.dbname", databaseName)
                .with("plugin.name", "pgoutput")
                .with("table.whitelist", TEST_TABLE_NAME).build();
    }
}