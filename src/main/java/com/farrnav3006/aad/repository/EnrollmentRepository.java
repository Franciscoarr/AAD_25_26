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
    // SQL statements
    private static final String SQL_CREATE = """
            INSERT INTO matricula (id_alumno, is_modulo, fecha)
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
    private final PostgresqlDriver postgresqlDriver;


    public Enrollment createEnrollment(Enrollment e) {
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_CREATE)) {

            ps.setInt(1, e.getStudentId());
            ps.setInt(2, e.getModuleId());
            ps.setDate(3, Date.valueOf(e.getDate()));
            ps.executeUpdate();
            log.info("create OK: {}", e);
            return e;
        } catch (SQLException er) {
            throw new RuntimeException("Error creating Enrollment", er);
        }
    }

    public List<Enrollment> findAll(){
        List<Enrollment> Enrollments = new ArrayList<>();
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FINDALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Enrollment enrollment = new Enrollment();
                    enrollment.setStudentId(rs.getInt("id_alumno"));
                    enrollment.setModuleId(rs.getInt("id_modulo"));
                    enrollment.setDate(rs.getDate("fecha").toLocalDate());
                    Enrollments.add(enrollment);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding Student ", e);
        }
        return Enrollments;
    }

    public List<Enrollment> findByStudent(int studentId) {
        List<Enrollment> Enrollments = new ArrayList<>();
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FINDBYSTUDENTID)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Enrollment enrollment = new Enrollment();
                    enrollment.setStudentId(rs.getInt("id_alumno"));
                    enrollment.setModuleId(rs.getInt("id_modulo"));
                    enrollment.setDate(rs.getDate("fecha").toLocalDate());
                    Enrollments.add(enrollment);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding Student id=" + studentId, e);
        }
        return Enrollments;
    }

    public boolean delete(int studentId, int moduleId) {
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, studentId);
            ps.setInt(1, moduleId);
            int deleted = ps.executeUpdate();
            boolean ok = deleted > 0;
            log.info("delete {} for Student id={} and Module id={}", ok ? "OK" : "NO", studentId, moduleId);
            return ok;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Student id=" + studentId, e);
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

