package com.farrnav3006.aad;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;

@SpringBootApplication
@RequiredArgsConstructor
public class JdbcApplication implements CommandLineRunner {
    private final PostgresqlDriver dataSource;
    
    public static void main(String[] args) {
        SpringApplication.run(JdbcApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Testing JDBC connection...");
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("Connection successful: " + conn.getMetaData().getURL());
            System.out.println("Database: " + conn.getMetaData().getDatabaseProductName());
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }
}
