package com.example.iprovider.security;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class provides configuration for the database connection pool using ComboPooledDataSource.
 */
@Configuration
public class DatabaseConfig {

    /**
     * This method creates and configures the ComboPooledDataSource.
     *
     * @return a new ComboPooledDataSource object
     * @throws Exception if an error occurs while setting up the data source
     */
    @Bean
    public ComboPooledDataSource dataSource() throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("org.postgresql.Driver");
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/iprovider");
        dataSource.setUser("postgres");
        dataSource.setPassword("admin");
        dataSource.setMaxPoolSize(10);
        dataSource.setMinPoolSize(2);
        dataSource.setInitialPoolSize(5);
        dataSource.setAcquireIncrement(1);
        dataSource.setAcquireRetryAttempts(3);
        dataSource.setAcquireRetryDelay(1000);
        dataSource.setTestConnectionOnCheckout(true);
        dataSource.setTestConnectionOnCheckin(true);

        dataSource.setConnectionCustomizerClassName(MyConnectionCustomizer.class.getName());

        return dataSource;
    }
}