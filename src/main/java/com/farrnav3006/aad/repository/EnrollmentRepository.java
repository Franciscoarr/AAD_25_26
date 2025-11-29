package com.farrnav3006.aad.repository;

import com.farrnav3006.aad.model.Enrollment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class EnrollmentRepository {
    // SQL statements
    private static final String SQL_CREATE = """
            INSERT INTO matricula (id_alumno, id_modulo, fecha)
            VALUES (?, ?, ?)
            """;
    private static final String SQL_FINDALL = """
            SELECT *
            FROM matricula
            """;
    private static final String SQL_FINDBYSTUDENTID = """
            SELECT *
            FROM matricula
            WHERE id_alumno = ?
            """;
    private static final String SQL_DELETE = """
            DELETE FROM matricula
            WHERE id_alumno = ? AND id_modulo = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    public Enrollment createEnrollment(Enrollment enrollment) {
        log.info("Creating enrollment - Student ID: {}, Module ID: {}, Date: {}",
                enrollment.getStudentId(), enrollment.getModuleId(), enrollment.getDate());

        jdbcTemplate.update(SQL_CREATE, enrollment.getStudentId(), enrollment.getModuleId(),
                enrollment.getDate());

        log.info("Enrollment created successfully");
        return enrollment;
    }

    public List<Enrollment> findAll() {
        log.info("Finding all enrollments");

        List<Enrollment> enrollments = jdbcTemplate.query(SQL_FINDALL, (rs, rowNum) -> new Enrollment(
                        rs.getInt("id_alumno"),
                        rs.getInt("id_modulo"),
                        rs.getDate("fecha").toLocalDate()
                )
        );

        log.info("FindAll enrollments OK");
        return enrollments;
    }

    public List<Enrollment> findByStudent(int studentId) {
        List<Enrollment> enrollments = jdbcTemplate.query(SQL_FINDBYSTUDENTID, (rs, rowNum) -> new Enrollment(
                        rs.getInt("id_alumno"),
                        rs.getInt("id_modulo"),
                        rs.getDate("fecha").toLocalDate()
                ),
                studentId);

        log.info("FindByStudent OK student ID: {}", studentId);
        return enrollments;
    }

    public boolean delete(int studentId, int moduleId) {
        log.info("Deleting enrollment - Student ID: {}, Module ID: {}", studentId, moduleId);

        int deleted = jdbcTemplate.update(SQL_DELETE, studentId, moduleId);
        boolean success = deleted > 0;

        log.info("Delete enrollment {} - Student ID: {}, Module ID: {}",
                success ? "OK" : "NOOP", studentId, moduleId);
        return success;
    }

    public int countEnrollments(int studentId) {
        log.info("Counting enrollments for student ID: {}", studentId);

        SimpleJdbcCall countEnrollmentsCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("count_enrollments");

        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("student_id", studentId);

        int count = countEnrollmentsCall.executeFunction(Integer.class, in);

        log.info("CountEnrollments OK - Student ID: {} has {} enrollments", studentId, count);
        return count;
    }
}

