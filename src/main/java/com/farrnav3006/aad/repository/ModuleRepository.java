package com.farrnav3006.aad.repository;

import com.farrnav3006.aad.config.PostgresqlDriver;
import com.farrnav3006.aad.model.Module;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ModuleRepository implements CrudRepository<Module>{
    // SQL statements
    private static final String SQL_INSERT = """
            INSERT INTO modulo (codigo, nombre, horas)
            VALUES (?, ?, ?)
            """;
    private static final String SQL_FINDALL = """
            SELECT *
            FROM modulo
            """;
    private static final String SQL_FINDBYID = """
            SELECT *
            FROM modulo
            WHERE id_modulo = ?
            """;
    private static final String SQL_UPDATE = """
            UPDATE modulo
            SET codigo = ?, nombre = ?, horas = ?
            WHERE id_modulo = ?
            """;
    private static final String SQL_DELETE = """
            DELETE FROM modulo
            WHERE id_modulo = ?
            """;
    private final PostgresqlDriver postgresqlDriver;

    @Override
    public Module insert(Module m) {
        if (m == null) throw new IllegalArgumentException("Module cannot be null");
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, m.getCode());
            ps.setString(2, m.getName());
            ps.setInt(3, m.getHours());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    m.setId(keys.getInt(1));
                }
            }

            log.info("create OK: {}", m);
            return m;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating Module", e);
        }
    }

    @Override
    public List<Module> findAll() {
        List<Module> modules = new ArrayList<>();
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FINDALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Module module = new Module(
                            rs.getInt("id_modulo"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getInt("horas")
                    );
                    modules.add(module);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding Module ", e);
        }
        return modules;
    }

    public Module findById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = postgresqlDriver.getConnection();
            ps = conn.prepareStatement(SQL_FINDBYID);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Module s = mapRow(rs);
                log.info("findById OK: {}", s);
                return s;
            } else {
                log.info("findById NOOP for id={}", id);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding Module id=" + id, e);
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                log.warn("Error closing ResultSet", e);
            }
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                log.warn("Error closing PreparedStatement", e);
            }
        }
    }

    @Override
    public Module update(Module m) {
        if (m == null || m.getId() == null) {
            throw new IllegalArgumentException("update requires a Module with non-null id");
        }
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, m.getCode());
            ps.setString(2, m.getName());
            ps.setInt(3, m.getHours());
            ps.setInt(4, m.getId());
            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("Module not found for update: id=" + m.getId());
            }
            log.info("update OK: {}", m);
            return m;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Module id=" + m.getId(), e);
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
            throw new RuntimeException("Error deleting Module id=" + id, e);
        }
    }

    private Module mapRow(ResultSet rs) throws SQLException {
        Module s = new Module();
        s.setId(rs.getInt("id_modulo"));
        s.setCode(rs.getString("codigo"));
        s.setName(rs.getString("nombre"));
        s.setHours(rs.getInt("horas"));
        return s;
    }
}