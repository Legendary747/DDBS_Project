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
;
@Configuration
@EnableJpaRepositories(
        basePackages = "com.ddbs.datacenter.repository.db3",
        entityManagerFactoryRef = "entityManagerFactoryThree",
        transactionManagerRef = "transactionManagerThree"
)
public class BackUpDb3Config {

    @Value("${spring.datasource.db3.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.db3.username}")
    private String username;

    @Value("${spring.datasource.db3.password}")
    private String password;

    @Bean
    public DataSource backUpDataSourceThree() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryThree(
            EntityManagerFactoryBuilder builder, @Qualifier("backUpDataSourceThree") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.ddbs.datacenter.entities")
                .persistenceUnit("db3")
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManagerThree(
            @Qualifier("entityManagerFactoryThree") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
