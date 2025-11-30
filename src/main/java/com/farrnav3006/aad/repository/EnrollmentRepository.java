package com.farrnav3006.aad.repository;

import com.farrnav3006.aad.config.PostgresqlDriver;
import com.farrnav3006.aad.model.Enrollment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class EnrollmentRepository {

    //SQL prepared statements
    //SQL to register a new enrollment linking a student and a module
    private static final String SQL_CREATE = """
            INSERT INTO matricula (id_alumno, id_modulo, fecha)
            VALUES (?, ?, ?)
            """;

    //SQL to retrieve all enrollments in the system
    private static final String SQL_FINDALL = """
            SELECT *
            FROM matricula
            """;

    //SQL to retrieve all enrollments associated with a specific student
    private static final String SQL_FINDBYSTUDENTID = """
            SELECT *
            FROM matricula
            WHERE id_alumno = ?
            """;

    //SQL to remove a specific enrollment (student-module pair)
    private static final String SQL_DELETE = """
            DELETE FROM matricula
            WHERE id_alumno = ? AND id_modulo = ?
            """;

    //Custom PostgreSQL driver providing DB connections
    private final PostgresqlDriver postgresqlDriver;

    //CREATE ENROLLMENT
    public Enrollment createEnrollment(Enrollment e) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = postgresqlDriver.getConnection();
            ps = conn.prepareStatement(SQL_CREATE);

            // Bind values to prepared statement
            ps.setInt(1, e.getStudentId());
            ps.setInt(2, e.getModuleId());
            ps.setDate(3, Date.valueOf(e.getDate()));

            // Execute INSERT command
            ps.executeUpdate();

            log.info("create OK: {}", e);
            return e;

        } catch (SQLException er) {
            throw new RuntimeException("Error creating Enrollment", er);

        } finally {
            try { if (ps != null) ps.close(); }
            catch (SQLException ex) { log.warn("Error closing PreparedStatement", ex); }
        }
    }

    //FIND ALL
    public List<Enrollment> findAll() {
        List<Enrollment> enrollments = new ArrayList<>();

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FINDALL);
             ResultSet rs = ps.executeQuery()) {

            //Build a list of Enrollment objects from DB results
            while (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setStudentId(rs.getInt("id_alumno"));
                enrollment.setModuleId(rs.getInt("id_modulo"));
                enrollment.setDate(rs.getDate("fecha").toLocalDate());
                enrollments.add(enrollment);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding Enrollments", e);
        }

        return enrollments;
    }

    //FIND BY STUDENT ID
    public List<Enrollment> findByStudent(int studentId) {
        List<Enrollment> enrollments = new ArrayList<>();

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FINDBYSTUDENTID)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Enrollment enrollment = new Enrollment();
                    enrollment.setStudentId(rs.getInt("id_alumno"));
                    enrollment.setModuleId(rs.getInt("id_modulo"));
                    enrollment.setDate(rs.getDate("fecha").toLocalDate());
                    enrollments.add(enrollment);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding Enrollments for student id=" + studentId, e);
        }

        return enrollments;
    }

    // DELETE
    public boolean delete(int studentId, int moduleId) {
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, studentId);
            ps.setInt(2, moduleId);

            int deleted = ps.executeUpdate();
            boolean ok = deleted > 0;

            log.info("delete {} for Student id={} and Module id={}",
                    ok ? "OK" : "NO", studentId, moduleId);

            return ok;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Enrollment: studentId=" + studentId, e);
        }
    }
}
