package com.farrnav3006.aad;

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

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
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
