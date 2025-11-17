package com.farrnav3006.aad.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

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
    @Value("${spring.datasource.driver-class-name:org.postgresql.Driver}")
    private String driverClassName;
    @Value("classpath*:sql/*.sql")
    private Resource[] scripts;

    private Connection connection;

    public Connection getConnection() throws SQLException {
        if (connection != null) return connection;
        return DriverManager.getConnection(url, username, password);
    }

    public void beginTransaction() throws SQLException {
        if (connection != null) throw new IllegalStateException("connection already active");
        connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(false);
    }

    public void commit() throws SQLException {
        if (connection == null) throw new IllegalStateException("No active connection");
        try {
            connection.commit();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("Close error: {}", e.getMessage());
            }
            connection = null;
        }
    }

    public void rollback() {
        if (connection == null) return;
        try {
            connection.rollback();
        } catch (SQLException e) {
            log.error("Rollback error: {}", e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("Close error: {}", e.getMessage());
            }
        }
    }

    @PostConstruct
    public void init() {
        log.info("Initializing database...");
        for (Resource script : scripts) {
            executeSql(script);
        }
        log.info("Database initialized successfully!");
    }

    private void executeSql(Resource resource) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String sql = reader.lines().collect(Collectors.joining("\n"));
            stmt.execute(sql);
            log.info("Executed script: {}", resource.getClass());
        } catch (Exception e) {
            log.error("Error executing script {}: {}",
                    resource.getClass(), e.getMessage());
        }
    }

}
