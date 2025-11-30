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
        Student miriam = new Student(null, "66280457T", "Miriam", "miriam@g.educaand.es");
        Module programacion = new Module(null, "0485", "Programación", 250);

        miriam = studentManagementService.createStudent(miriam);
        programacion = studentManagementService.createModule(programacion);

        int modulosMatriculados = studentManagementService.countEnrollments(miriam.getId());
        log.info("{} módulos matriculados para el alumno {}", modulosMatriculados, miriam.getName());

        studentManagementService.enrollStudentInModule(miriam.getId(), programacion.getId());
        studentRepository.delete(miriam.getId());
    }

}



