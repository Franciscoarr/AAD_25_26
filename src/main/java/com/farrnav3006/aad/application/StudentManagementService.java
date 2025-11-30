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

    //CREATE MODULE
    @Override
    public Module createModule(Module module) {
        try {
            Module result;

            //If ID is null → create a new module
            if (module.getId() == null) {
                result = moduleRepository.insert(module);

            } else {
                //If module exists, return it; if not, create it
                Module existing = moduleRepository.findById(module.getId());
                result = (existing != null) ? existing : moduleRepository.insert(module);
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Error creating module", e);
        }
    }

    //CREATE STUDENT
    @Override
    public Student createStudent(Student student) {

        //Validation
        if (student.getName() == null || student.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (student.getNif() == null || student.getNif().isBlank()) {
            throw new IllegalArgumentException("NIF is required");
        }

        try {
            Student result;

            if (student.getId() == null) {
                //Insert new student
                result = studentRepository.insert(student);

            } else {
                //If student exists, return it; if not, create it
                Student existing = studentRepository.findById(student.getId());
                result = (existing != null) ? existing : studentRepository.insert(student);
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Error creating student", e);
        }
    }

    //ENROLL STUDENT IN MODULE
    @Override
    public Enrollment enrollStudentInModule(Integer studentId, Integer moduleId) {

        if (studentId == null) throw new IllegalArgumentException("Student ID cannot be null");
        if (moduleId == null) throw new IllegalArgumentException("Module ID cannot be null");

        try {
            postgresqlDriver.beginTransaction();

            Student student = studentRepository.findById(studentId);
            if (student == null)
                throw new IllegalArgumentException("Student not found: " + studentId);

            Module module = moduleRepository.findById(moduleId);
            if (module == null)
                throw new IllegalArgumentException("Module not found: " + moduleId);

            Enrollment created = enrollmentRepository.createEnrollment(
                    new Enrollment(student.getId(), module.getId(), LocalDate.now())
            );

            postgresqlDriver.commit();
            return created;

        } catch (Exception e) {
            postgresqlDriver.rollback();
            throw new RuntimeException("Error enrolling student in module", e);
        }
    }

    //CALL STORED FUNCTION
    public int countEnrollments(int studentId) {

        try (Connection conn = postgresqlDriver.getConnection();
             CallableStatement cs = conn.prepareCall("{ ? = call count_enrollments(?) }")) {

            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, studentId);

            cs.execute();

            return cs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException("Error counting enrollments", e);
        }
    }
}
