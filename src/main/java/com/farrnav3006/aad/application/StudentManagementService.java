package com.farrnav3006.aad.application;

import com.farrnav3006.aad.model.Enrollment;
import com.farrnav3006.aad.model.Module;
import com.farrnav3006.aad.model.Student;
import com.farrnav3006.aad.repository.CustomService;
import com.farrnav3006.aad.repository.EnrollmentRepository;
import com.farrnav3006.aad.repository.ModuleRepository;
import com.farrnav3006.aad.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentManagementService implements CustomService<Student> {

    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final EnrollmentRepository enrollmentRepository;

    public Module createModule(Module module) {
        Module existing = moduleRepository.findById(module.getId());
        if (existing != null) {
            return existing;
        } else {
            return moduleRepository.insert(module);
        }
    }

    public Student createStudent(Student student) {
        if (student.getName() == null || student.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (student.getNif() == null || student.getNif().isBlank()) {
            throw new IllegalArgumentException("NIF is required");
        }

        Student existing = studentRepository.findById(student.getId());
        if (existing != null) {
            return existing;
        } else {
            return studentRepository.insert(student);
        }
    }

    @Transactional
    public Enrollment enrollStudentInModule(Integer studentId, Integer moduleId) {
        var student = studentRepository.findById(studentId);
        if (student == null) throw new IllegalArgumentException("Student not found: " + studentId);

        var module = moduleRepository.findById(moduleId);
        if (module == null) throw new IllegalArgumentException("Module not found: " + moduleId);

        return enrollmentRepository.createEnrollment(
                new Enrollment(student.getId(), module.getId(), LocalDate.now())
        );
    }

    public int getEnrollmentCount(Integer studentId) {
        return enrollmentRepository.countEnrollments(studentId);
    }

    // Implementación de métodos de CustomService<Student>
    @Override
    public Student insert(Student student) {
        return createStudent(student);
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student findById(Integer id) {
        return studentRepository.findById(id);
    }

    @Override
    public Student update(Student student) {
        Student existingStudent = studentRepository.findById(student.getId());
        if (existingStudent == null) {
            throw new IllegalArgumentException("Student not found with id: " + student.getId());
        }
        return studentRepository.update(student);
    }

    @Override
    public boolean delete(Integer id) {
        studentRepository.delete(id);
        return true;
    }
}