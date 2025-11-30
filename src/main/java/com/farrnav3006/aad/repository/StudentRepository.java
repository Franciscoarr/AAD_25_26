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

    //SQL prepared statements
    //SQL template for inserting new students
    private static final String SQL_INSERT = """
            INSERT INTO alumno (nif, nombre, email)
            VALUES (?, ?, ?)
            """;

    //SQL to retrieve all students
    private static final String SQL_FINDALL = """
            SELECT *
            FROM alumno
            """;

    //SQL to retrieve a student by its ID
    private static final String SQL_FINDBYID = """
            SELECT *
            FROM alumno
            WHERE id_alumno = ?
            """;

    //SQL to update an existing student
    private static final String SQL_UPDATE = """
            UPDATE alumno
            SET nif = ?, nombre = ?, email = ?
            WHERE id_alumno = ?
            """;

    //SQL to delete a student by ID
    private static final String SQL_DELETE = """
            DELETE FROM alumno
            WHERE id_alumno = ?
            """;

    //PostgreSQL connection provider
    private final PostgresqlDriver postgresqlDriver;

    //INSERT
    @Override
    public Student insert(Student s) {
        if (s == null) throw new IllegalArgumentException("Student cannot be null");

        try (Connection conn = postgresqlDriver.getConnection();
             //Prepare statement that returns generated keys
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            //Bind parameters to SQL statement
            ps.setString(1, s.getNif());
            ps.setString(2, s.getName());
            ps.setString(3, s.getEmail());

            //Execute INSERT command
            ps.executeUpdate();

            //Retrieve auto-generated primary key (id_alumno)
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    s.setId(keys.getInt(1));
                }
            }

            log.info("create OK: {}", s);
            return s;

        } catch (SQLException e) {
            //Wrap SQL errors in runtime exception
            throw new RuntimeException("Error creating Student", e);
        }
    }

    //FIND ALL
    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FINDALL)) {

            //Execute SELECT query
            try (ResultSet rs = ps.executeQuery()) {

                //Iterate over each row from the database result
                while (rs.next()) {
                    Student student = new Student();

                    //Map each column to Student properties
                    student.setId(rs.getInt("id_alumno"));
                    student.setNif(rs.getString("nif"));
                    student.setName(rs.getString("nombre"));
                    student.setEmail(rs.getString("email"));

                    students.add(student);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding Student ", e);
        }

        return students;
    }


    //FIND BY ID
    @Override
    public Student findById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //Open DB connection
            conn = postgresqlDriver.getConnection();

            //Prepare SELECT query
            ps = conn.prepareStatement(SQL_FINDBYID);
            ps.setInt(1, id);

            //Execute query
            rs = ps.executeQuery();

            //Check if result exists
            if (rs.next()) {
                Student s = mapRow(rs); //Map row to Student object
                log.info("findById OK: {}", s);
                return s;
            } else {
                log.info("findById NOOP for id={}", id);
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding Student id=" + id, e);

        } finally {
            //Ensure resources are closed (manual because not using try-with-resources here)
            try { if (rs != null) rs.close(); } catch (SQLException e) { log.warn("Error closing RS", e); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { log.warn("Error closing PS", e); }
        }
    }

    //UPDATE
    @Override
    public Student update(Student s) {
        if (s == null || s.getId() == null) {
            throw new IllegalArgumentException("update requires a Student with non-null id");
        }

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            //Bind updated values
            ps.setString(1, s.getNif());
            ps.setString(2, s.getName());
            ps.setString(3, s.getEmail());
            ps.setInt(4, s.getId());

            //Execute UPDATE
            int updated = ps.executeUpdate();

            //Check if record existed
            if (updated == 0) {
                throw new RuntimeException("Student not found for update: id=" + s.getId());
            }

            log.info("update OK: {}", s);
            return s;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating Student id=" + s.getId(), e);
        }
    }

    //DELETE
    @Override
    public boolean delete(int id) {
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);

            //Execute DELETE query
            int deleted = ps.executeUpdate();

            //Return true if a row was removed
            boolean ok = deleted > 0;
            log.info("delete {} for id={}", ok ? "OK" : "NOOP", id);

            return ok;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Student id=" + id, e);
        }
    }


    //Helper method: map DB row → Student object
    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();

        //Extract each column from the ResultSet
        s.setId(rs.getInt("id_alumno"));
        s.setNif(rs.getString("nif"));
        s.setName(rs.getString("nombre"));
        s.setEmail(rs.getString("email"));

        return s;
    }
}

