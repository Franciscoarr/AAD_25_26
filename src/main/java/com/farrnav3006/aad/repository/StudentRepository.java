package com.farrnav3006.aad.repository;

import com.farrnav3006.aad.config.PostgresqlDriver;
import com.farrnav3006.aad.model.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class StudentRepository implements CustomService<Student> {
    // SQL statements
    private static final String SQL_INSERT = """
            INSERT INTO alumno (nif, nombre, email)
            VALUES (?, ?, ?, ?)
            """;
    private static final String SQL_FINDALL = """
            SELECT *
            FROM alumno
            """;
    private static final String SQL_FINDBYID = """
            SELECT *
            FROM alumno
            WHERE id_alumno = ?
            """;
    private static final String SQL_UPDATE = """
            UPDATE alumno
            SET nif = ?, nombre = ?, email = ?
            WHERE id_alumno = ?
            """;
    private static final String SQL_DELETE = """
            DELETE FROM alumno
            WHERE id_alumno = ?
            """;
    private final PostgresqlDriver postgresqlDriver;


    @Override
    public Student insert(Student s) {
        if (s == null) throw new IllegalArgumentException("Student cannot be null");
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, s.getNif());
            ps.setString(2, s.getName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getCurse());
            ps.setObject(5, s.getModules(), Types.ARRAY);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    s.setId(keys.getInt(1));
                }
            }
            log.info("create OK: {}", s);
            return s;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating Student", e);
        }
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FINDALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student(
                            rs.getInt("id_alumno"),
                            rs.getString("nif"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("curse"),
                            new ArrayList<>() // Los módulos se cargan por separado
                    );
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding Student ", e);
        }
        return students;
    }

    @Override
    public Student findById(int id) {
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FINDBYID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Student s = new Student();
                    s.setId(rs.getInt("id_alumno"));
                    s.setNif(rs.getString("nif"));
                    s.setName(rs.getString("nombre"));
                    s.setEmail(rs.getString("email"));
                    s.setCurse(rs.getString("curse"));
                    s.setModules(rs.getObject("modules", java.util.List.class));
                    log.info("findById OK: {}", s);
                    return s;
                } else {
                    log.info("findById NOOP for id={}", id);
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding Student id=" + id, e);
        }
    }

    @Override
    public Student update(Student s) {
        if (s == null || s.getId() == null) {
            throw new IllegalArgumentException("update requires a Student with non-null id");
        }
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, s.getNif());
            ps.setString(2, s.getName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getCurse());
            ps.setObject(5, s.getModules(), Types.ARRAY);
            ps.setInt(6, s.getId());
            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("Student not found for update: id=" + s.getId());
            }
            log.info("update OK: {}", s);
            return s;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Student id=" + s.getId(), e);
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            int deleted = ps.executeUpdate();
            boolean ok = deleted > 0;
            log.info("delete {} for id={}", ok ? "OK" : "NOOP", id);
            return ok;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Student id=" + id, e);
        }
    }
}

