package com.farrnav3006.aad.application;

import com.farrnav3006.aad.config.PostgresqlDriver;
import com.farrnav3006.aad.model.Enrollment;
import com.farrnav3006.aad.model.Module;
import com.farrnav3006.aad.model.Student;
import com.farrnav3006.aad.repository.CustomService;
import com.farrnav3006.aad.repository.EnrollmentRepository;
import com.farrnav3006.aad.repository.ModuleRepository;
import com.farrnav3006.aad.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StudentManagementService implements CustomService<Student>{

    private final PostgresqlDriver postgresqlDriver;
    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final EnrollmentRepository enrollmentRepository;

    /**
     * Crea un módulo si no existe (por código). Si existe, lo devuelve.
     */
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

    /**
     * Crea un estudiante si no existe. Valida campos básicos.
     */
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

    /**
     * Matricula a un estudiante en un módulo, controlando manualmente la transacción.
     */
    public Enrollment enrollStudentInModule(Integer studentId, Integer moduleId) {
        try {
            postgresqlDriver.beginTransaction();

            var student = studentRepository.findById(studentId);
            if (student == null) throw new IllegalArgumentException("Student not found: " + studentId);

            var module = moduleRepository.findById(moduleId);
            if (module == null) throw new IllegalArgumentException("Module not found: " + moduleId);

            Enrollment created = enrollmentRepository.createEnrollment(new Enrollment(null, student.getId(), module.getId(), LocalDate.now()));

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

    // Implementación de métodos de CustomService<Student>

    @Override
    public Student insert(Student student) {
        return createStudent(student);
    }

    @Override
    public List<Student> findAll() {
        try {
            return studentRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all students", e);
        }
    }

    @Override
    public Student findById(Integer id) {
        try {
            return studentRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error finding student with id: " + id, e);
        }
    }

    @Override
    public Student update(Student student) {
        try {
            // Verificar que el estudiante existe antes de actualizar
            Student existingStudent = studentRepository.findById(student.getId());
            if (existingStudent == null) {
                throw new IllegalArgumentException("Student not found with id: " + student.getId());
            }

            return studentRepository.update(student);
        } catch (Exception e) {
            throw new RuntimeException("Error updating student", e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try {
            studentRepository.delete(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting student with id: " + id, e);
        }
    }

}