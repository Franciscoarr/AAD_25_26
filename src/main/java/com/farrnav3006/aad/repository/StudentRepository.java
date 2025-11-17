package com.farrnav3006.aad.repository;

import com.farrnav3006.aad.config.PostgresqlDriver;
import com.farrnav3006.aad.model.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
@Slf4j
@RequiredArgsConstructor
public class StudentRepository implements CrudRepository<Student> {
    // SQL statements
    private static final String SQL_INSERT = """
            INSERT INTO student (first_name, last_name, birth_date, average_grade)
            VALUES (?, ?, ?, ?)
            """;
    private static final String SQL_SELECT_BY_ID = """
            SELECT id, first_name, last_name, birth_date, average_grade
            FROM student
            WHERE id = ?
            """;
    private static final String SQL_UPDATE = """
            UPDATE student
            SET first_name = ?, last_name = ?, birth_date = ?, average_grade = ?
            WHERE id = ?
            """;
    private static final String SQL_DELETE = """
            DELETE FROM student
            WHERE id = ?
            """;
    private final PostgresqlDriver postgresqlDriver;


    @Override
    public Student create(Student entity) {
        if (entity == null) throw new IllegalArgumentException("Student cannot be null");
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, entity.getName());
            ps.setString(2, entity.getNif());
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
    public Student read(Student entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("read requires a Student with non-null id");
        }
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, entity.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Student found = mapRow(rs);
                    log.info("read OK: {}", found);
                    return found;
                } else {
                    log.info("read: no student found with id={}", entity.getId());
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading Student id=" + entity.getId(), e);
        }
    }

    @Override
    public Student update(Student entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("update requires a Student with non-null id");
        }
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getNif());
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
        s.setId(rs.getInt("id"));
        s.setFirstName(rs.getString("first_name"));
        s.setLastName(rs.getString("last_name"));
        Date bd = rs.getDate("birth_date");
        s.setBirthDate(bd != null ? Date.valueOf(bd.toLocalDate()) : null);
        // NUMERIC maps fine to BigDecimal; if you use Double in the model, adjust accordingly:
        // For example, rs.getBigDecimal("average_grade") != null ?rs.getBigDecimal("average_grade").doubleValue() : null
        s.setAverageGrade(rs.getBigDecimal("average_grade") != null
                ? rs.getBigDecimal("average_grade").doubleValue()
                : 0.0);
        return s;
    }
}

