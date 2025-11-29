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
        jdbcTemplate.update(SQL_CREATE, enrollment.getStudentId(), enrollment.getModuleId(),
                enrollment.getDate());
        return enrollment;
    }

    public List<Enrollment> findAll() {
        return jdbcTemplate.query(SQL_FINDALL, (rs, rowNum) -> new Enrollment(
                rs.getInt("id_alumno"),
                rs.getInt("id_modulo"),
                rs.getDate("fecha").toLocalDate()
            )
        );
    }

    public List<Enrollment> findByStudent(int studentId) {
        return jdbcTemplate.query(SQL_FINDBYSTUDENTID, (rs, rowNum) -> new Enrollment(
                        rs.getInt("id_alumno"),
                        rs.getInt("id_modulo"),
                        rs.getDate("fecha").toLocalDate()
                ),
                studentId);
    }

    public boolean delete(int studentId, int moduleId) {
        int deleted = jdbcTemplate.update(SQL_DELETE, studentId, moduleId);
        return deleted > 0;
    }

    public int countEnrollments(int studentId) {
        SimpleJdbcCall countEnrollmentsCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("count_enrollments");

        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("student_id", studentId);

        return countEnrollmentsCall.executeFunction(Integer.class, in);
    }
}

