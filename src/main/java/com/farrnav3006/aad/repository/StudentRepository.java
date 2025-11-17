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
public class StudentRepository implements CrudRepository<Student> {
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
    public Student insert(Student entity) {
        if (entity == null) throw new IllegalArgumentException("Student cannot be null");
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, entity.getNif());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getCurse());
            ps.setObject(5, entity.getModules(), Types.ARRAY);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setId(keys.getInt(1));
                }
            }
            log.info("create OK: {}", entity);
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating Student", e);
        }
    }

    @Override
    public List<Student> findAll(){
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
    public Student findById(Student entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("findById requires a Student with non-null id");
        }
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FINDBYID)) {
            ps.setInt(1, entity.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Student s = mapRow(rs);
                    log.info("findById OK: {}", s);
                    return s;
                } else {
                    log.info("findById NOOP for id={}", entity.getId());
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding Student id=" + entity.getId(), e);
        }
    }

    @Override
    public Student update(Student entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("update requires a Student with non-null id");
        }
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, entity.getNif());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getCurse());
            ps.setObject(5, entity.getModules(), Types.ARRAY);
            ps.setInt(6, entity.getId());
            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("Student not found for update: id=" + entity.getId());
            }
            log.info("update OK: {}", entity);
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Student id=" + entity.getId(), e);
        }
    }

    @Override
    public boolean delete(Student entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("delete requires a Student with non-null id");
        }
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, entity.getId());
            int deleted = ps.executeUpdate();
            boolean ok = deleted > 0;
            log.info("delete {} for id={}", ok ? "OK" : "NOOP", entity.getId());
            return ok;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Student id=" + entity.getId(), e);
        }
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getInt("id_alumno"));
        s.setNif(rs.getString("nif"));
        s.setName(rs.getString("nombre"));
        s.setEmail(rs.getString("email"));
        s.setCurse(rs.getString("curse"));
        s.setModules(rs.getObject("modules", java.util.List.class));
        return s;
    }
}

