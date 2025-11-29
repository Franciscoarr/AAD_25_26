package com.farrnav3006.aad.repository;

import com.farrnav3006.aad.model.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class StudentRepository {
    // SQL statements
    private static final String SQL_INSERT = """
            INSERT INTO alumno (nif, nombre, email)
            VALUES (?, ?, ?)
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
    private final JdbcTemplate jdbcTemplate;

    public Student insert(Student student) {
        String sql = "INSERT INTO alumno (nif, nombre, email) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, student.getNif(), student.getName(), student.getEmail());
        return student;
    }

    public List<Student> findAll() {
        return jdbcTemplate.query(
                "SELECT id_alumno, nif, nombre, email FROM alumno",
                (rs, rowNum) -> new Student(
                        rs.getInt("id_alumno"),
                        rs.getString("nif"),
                        rs.getString("nombre"),
                        rs.getString("email")
                )
        );
    }

    public Student findById(int id) {
        List<Student> students = jdbcTemplate.query(
                "SELECT id_alumno, nif, nombre, email FROM alumno WHERE id_alumno = ?",
                (rs, rowNum) -> new Student(
                        rs.getInt("id_alumno"),
                        rs.getString("nif"),
                        rs.getString("nombre"),
                        rs.getString("email")
                ),
                id
        );
        return students.isEmpty() ? null : students.get(0);
    }

    public Student update(Student student) {
        String sql = "UPDATE alumno SET nif = ?, nombre = ?, email = ? WHERE id_alumno = ?";
        int updated = jdbcTemplate.update(sql, student.getNif(), student.getName(),
                student.getEmail(), student.getId());
        if (updated == 0) {
            throw new RuntimeException("Student not found for update: id=" + student.getId());
        }
        return student;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM alumno WHERE id_alumno = ?";
        int deleted = jdbcTemplate.update(sql, id);
        return deleted > 0;
    }
}

