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
            Module existing = moduleRepository.findById(module.getId());
            if (existing != null) {
                return existing;
            } else {
                return moduleRepository.insert(module);
            }
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
            Student existing = studentRepository.findById(student.getId());
            if (existing != null) {
                return existing;
            } else {
                return studentRepository.insert(student);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating student", e);
        }
    }

    @Override
    public Enrollment enrollStudentInModule(Integer studentId, Integer moduleId) {
        try {
            postgresqlDriver.beginTransaction();

            var student = studentRepository.findById(studentId);
            if (student == null) throw new IllegalArgumentException("Student not found: " + studentId);

            var module = moduleRepository.findById(moduleId);
            if (module == null) throw new IllegalArgumentException("Module not found: " + moduleId);

            Enrollment created = enrollmentRepository.createEnrollment(new Enrollment(student.getId(), module.getId(), LocalDate.now()));

            postgresqlDriver.commit();
            return created;

        } catch (Exception e) {
            postgresqlDriver.rollback();
            throw new RuntimeException("Error enrolling student in module", e);
        }
    }

    /**
     * Invoca repositorio para contar matrículas (función almacenada).
     */
    public int getEnrollmentCount(Integer studentId) {
        try {
            return enrollmentRepository.countEnrollments(studentId);
        } catch (Exception e) {
            throw new RuntimeException("Error counting enrollments", e);
        }
    }

}