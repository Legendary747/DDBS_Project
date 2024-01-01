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
        basePackages = "com.ddbs.datacenter.repository.db4",
        entityManagerFactoryRef = "entityManagerFactoryFour",
        transactionManagerRef = "transactionManagerFour"
)
public class BackUpDb4Config {

    @Value("${spring.datasource.db4.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.db4.username}")
    private String username;

    @Value("${spring.datasource.db4.password}")
    private String password;

    @Bean
    public DataSource backUpDataSourceFour() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryFour(
            EntityManagerFactoryBuilder builder, @Qualifier("backUpDataSourceFour") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.ddbs.datacenter.entities")
                .persistenceUnit("db4")
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManagerFour(
            @Qualifier("entityManagerFactoryFour") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}

