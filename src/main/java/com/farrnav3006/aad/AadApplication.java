package com.farrnav3006.aad;

import com.farrnav3006.aad.model.Student;
import com.farrnav3006.aad.repository.CrudRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class AadApplication implements CommandLineRunner {

    private final PostgresqlDriver postgresqlDriver;
    private final CrudRepository<Student> repo;

    public static void main(String[] args) {
        SpringApplication.run(AadApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // CREATE
        Student s = new Student();
        s.setFirstName("Lucia");
        s.setLastName("Martinez");
        s.setBirthDate(Date.valueOf(LocalDate.of(2004, 5, 10)));
        s.setAverageGrade(8.7);
        s = repo.create(s);
        // READ
        Student probe = new Student();
        probe.setId(s.getId());
        Student loaded = repo.read(probe);
        // UPDATE
        loaded.setAverageGrade(9.2);
        repo.update(loaded);
        // DELETE
        repo.delete(loaded);

        //SEGUNDA
        Student s2 = new Student();
        s2.setFirstName("Ruben");
        s2.setLastName("Dominguez");
        s2.setBirthDate(Date.valueOf(LocalDate.of(2004, 5, 10)));
        s2.setAverageGrade(8.7);
        s2 = repo.create(s2);
        // READ
        Student probe2 = new Student();
        probe2.setId(s.getId());
        Student loaded2 = repo.read(probe2);
        // UPDATE
        loaded2.setAverageGrade(9.2);
        repo.update(loaded2);
        // DELETE
        repo.delete(loaded2);

        log.info("Testing JDBC connection...");
        try (Connection conn = postgresqlDriver.getConnection()) {
            log.info("Connection successful: {}",
                    conn.getMetaData().getURL());
            log.info("Database: {}",
                    conn.getMetaData().getDatabaseProductName());
        } catch (Exception e) {
            log.info("Connection failed: " + e.getMessage());
        }
    }
}


