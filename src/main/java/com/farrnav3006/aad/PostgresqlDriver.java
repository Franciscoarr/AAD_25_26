package com.farrnav3006.aad;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
@Slf4j
public class PostgresqlDriver {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    // Si no se define, por defecto usamos el driver de PostgreSQL
    @Value("${spring.datasource.driver-classname:org.postgresql.Driver}")
    private String driverClassName;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
