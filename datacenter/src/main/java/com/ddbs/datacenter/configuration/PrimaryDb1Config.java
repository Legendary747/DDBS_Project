package com.ddbs.datacenter.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class PrimaryDb1Config {

    @Value("${spring.datasource.primary_1.jdbc-url}")
    private String jdbcUrl;

    @Value("${spring.datasource.primary_1.username}")
    private String username;

    @Value("${spring.datasource.primary_1.password}")
    private String password;

    @Bean
    public DataSource primaryDataSourceOne() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
