package com.farrnav3006.aad;

import com.farrnav3006.aad.application.StudentManagementService;
import com.farrnav3006.aad.config.PostgresqlDriver;
import com.farrnav3006.aad.model.Student;
import com.farrnav3006.aad.model.Module;
import com.farrnav3006.aad.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class Application implements CommandLineRunner {

    private final PostgresqlDriver postgresqlDriver;
    private final StudentManagementService studentManagementService;
    private final StudentRepository studentRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting student management system test...");

        Student miriam = new Student(1, "66280457T", "Miriam",
                "miriam@g.educaand.es", "DAM", List.of());
        Module programacion = new Module(4, "0485", "Programacion", 250);

        miriam = studentManagementService.createStudent(miriam);
        programacion = studentManagementService.createModule(programacion);

        studentManagementService.enrollStudentInModule(miriam.getId(), programacion.getId());

        // Count enrollments for the student
        int enrollmentCount = studentManagementService.getEnrollmentCount(miriam.getId());
        System.out.println("Total enrollments for student: " + enrollmentCount);

        studentRepository.delete(miriam.getId());

        System.out.println("Test completed successfully!");
    }

}



