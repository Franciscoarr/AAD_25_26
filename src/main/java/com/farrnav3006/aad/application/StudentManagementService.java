package com.farrnav3006.aad.application;

import com.farrnav3006.aad.model.Enrollment;
import com.farrnav3006.aad.model.Module;
import com.farrnav3006.aad.model.Student;
import com.farrnav3006.aad.repository.EnrollmentRepository;
import com.farrnav3006.aad.repository.ModuleRepository;
import com.farrnav3006.aad.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentManagementService implements CustomService{

    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public Module createModule(Module module) {
        try {
            Module created = moduleRepository.insert(module);
            log.info("Module created with ID: {}", created.getId());
            return created;
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

        Student created = studentRepository.insert(student);

        log.info("Student created with ID: {}", created.getId());
        return created;
    }

    @Transactional
    @Override
    public Enrollment enrollStudentInModule(Integer studentId, Integer moduleId) {
        var student = studentRepository.findById(studentId);
        if (student == null) throw new IllegalArgumentException("Student not found: " + studentId);

        var module = moduleRepository.findById(moduleId);
        if (module == null) throw new IllegalArgumentException("Module not found: " + moduleId);

        return enrollmentRepository.createEnrollment(
                new Enrollment(student.getId(), module.getId(), LocalDate.now())
        );
    }
}