package com.farrnav3006.aad.application;

import com.farrnav3006.aad.config.PostgresqlDriver;
import com.farrnav3006.aad.model.Enrollment;
import com.farrnav3006.aad.model.Module;
import com.farrnav3006.aad.model.Student;
import com.farrnav3006.aad.repository.EnrollmentRepository;
import com.farrnav3006.aad.repository.ModuleRepository;
import com.farrnav3006.aad.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;



@Service
@RequiredArgsConstructor
public class StudentManagementService implements CustomService {

    private final PostgresqlDriver postgresqlDriver;
    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public Module createModule(Module module) {
        try {
            System.out.println("Creating module - Input ID: " + module.getId() + ", Code: " + module.getCode());

            Module result;
            if (module.getId() == null) {
                result = moduleRepository.insert(module);
            } else {
                Module existing = moduleRepository.findById(module.getId());
                result = existing != null ? existing : moduleRepository.insert(module);
            }

            System.out.println("Created module - Output ID: " + result.getId() + ", Code: " + result.getCode());
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Error creating module", e);
        }
    }

    @Override
    public Student createStudent(Student student) {
        if (student.getName() == null || student.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (student.getNif() == null || student.getNif().isBlank()) {
            throw new IllegalArgumentException("NIF is required");
        }

        try {
            System.out.println("Creating student - Input ID: " + student.getId() + ", Name: " + student.getName());

            Student result;
            if (student.getId() == null) {
                result = studentRepository.insert(student);
            } else {
                Student existing = studentRepository.findById(student.getId());
                result = existing != null ? existing : studentRepository.insert(student);
            }

            System.out.println("Created student - Output ID: " + result.getId() + ", Name: " + result.getName());
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Error creating student", e);
        }
    }

    @Override
    public Enrollment enrollStudentInModule(Integer studentId, Integer moduleId) {
        // Validar parámetros
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
        if (moduleId == null) {
            throw new IllegalArgumentException("Module ID cannot be null");
        }

        System.out.println("Enrolling - Student ID: " + studentId + ", Module ID: " + moduleId);

        try {
            postgresqlDriver.beginTransaction();

            var student = studentRepository.findById(studentId);
            if (student == null) throw new IllegalArgumentException("Student not found: " + studentId);
            System.out.println("Found student: " + student.getId() + " - " + student.getName());

            var module = moduleRepository.findById(moduleId);
            if (module == null) throw new IllegalArgumentException("Module not found: " + moduleId);
            System.out.println("Found module: " + module.getId() + " - " + module.getName());

            Enrollment created = enrollmentRepository.createEnrollment(new Enrollment(student.getId(), module.getId(), LocalDate.now()));
            System.out.println("Created enrollment: " + created.getStudentId() + " -> " + created.getModuleId());

            postgresqlDriver.commit();
            return created;

        } catch (Exception e) {
            postgresqlDriver.rollback();
            throw new RuntimeException("Error enrolling student in module", e);
        }
    }

    public int countEnrollments(int studentId) {
        try (Connection conn = postgresqlDriver.getConnection();
             CallableStatement cs = conn.prepareCall("{ ? = call count_enrollments(?) }")) {
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, studentId);
            cs.execute();
            return cs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Error count Enrollment", e);
        }
    }
}