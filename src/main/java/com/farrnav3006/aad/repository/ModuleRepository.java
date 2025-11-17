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
public class ModuleRepository implements CrudRepository<Module> {
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
            UPDATE alumno
            SET codigo = ?, nombre = ?, horas = ?
            WHERE id_modulo = ?
            """;
    private static final String SQL_DELETE = """
            DELETE FROM modulo
            WHERE id_modulo = ?
            """;
    private final PostgresqlDriver postgresqlDriver;


    @Override
    public Module insert(Module entity) {
        if (entity == null) throw new IllegalArgumentException("Student cannot be null");
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, entity.getCode());
            ps.setString(2, entity.getName());
            ps.setInt(3, entity.getHours());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setId(keys.getInt(1));
                }
            }
            log.info("create OK: {}", entity);
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating Module", e);
        }
    }

    @Override
    public List<Module> findAll(){
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

    @Override
    public Module findById(Module entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("findById requires a Module with non-null id");
        }
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FINDBYID)) {
            ps.setInt(1, entity.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Module s = mapRow(rs);
                    log.info("findById OK: {}", s);
                    return s;
                } else {
                    log.info("findById NOOP for id={}", entity.getId());
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding Module id=" + entity.getId(), e);
        }
    }

    @Override
    public Module update(Module entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("update requires a Module with non-null id");
        }
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, entity.getCode());
            ps.setString(2, entity.getName());
            ps.setInt(3, entity.getHours());
            ps.setInt(6, entity.getId());
            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("Module not found for update: id=" + entity.getId());
            }
            log.info("update OK: {}", entity);
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Module id=" + entity.getId(), e);
        }
    }

    @Override
    public boolean delete(Module entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("delete requires a Module with non-null id");
        }
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, entity.getId());
            int deleted = ps.executeUpdate();
            boolean ok = deleted > 0;
            log.info("delete {} for id={}", ok ? "OK" : "NOOP", entity.getId());
            return ok;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Module id=" + entity.getId(), e);
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