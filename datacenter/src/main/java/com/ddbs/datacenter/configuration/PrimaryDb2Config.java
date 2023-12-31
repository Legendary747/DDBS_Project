package com.ddbs.datacenter.configuration;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.ddbs.datacenter.repository.db2",
        entityManagerFactoryRef = "entityManagerFactoryTwo",
        transactionManagerRef = "transactionManagerTwo"
)
public class PrimaryDb2Config {

    @Value("${spring.datasource.db2.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.db2.username}")
    private String username;

    @Value("${spring.datasource.db2.password}")
    private String password;

    @Bean
    public DataSource primaryDataSourceTwo() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryTwo(
            EntityManagerFactoryBuilder builder, @Qualifier("primaryDataSourceTwo") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.ddbs.datacenter.entities")
                .persistenceUnit("db2")
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManagerTwo(
            @Qualifier("entityManagerFactoryTwo") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
