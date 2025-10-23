package com.farrnav3006.aad;

import com.farrnav3006.aad.application.StudentService;
import com.farrnav3006.aad.model.Module;
import com.farrnav3006.aad.model.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class AadApplication implements CommandLineRunner {

    private final StudentService studentService;

    public static void main(String[] args) {
        SpringApplication.run(AadApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Student student = new Student("123345678", "Farrnav", "AAA", "Cocina");
        Module module = new Module("M01", "Base de Datos");
        Module module2 = new Module("M02", "Programacion");
        List<Module> modules = List.of(module, module2);
        Student create = studentService.createStudent(student, modules);
        if (create != null) {
            log.info("Student created: " + create.toString());
        } else {
            log.error("Student not created");
        }
    }
}

