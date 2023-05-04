package ru.clevertec.zabalotcki.config;

import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@PropertySource("classpath:application.yml")
public class DataSourceConfig {

    private final String DRIVER = "org.postgresql.Driver";
    private final String USERNAME = "gifts";
    private final String URL = "jdbc:postgresql://localhost:5432/gifts";
    private final String PASSWORD = "password";

    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER);
        dataSource.setUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }

}
