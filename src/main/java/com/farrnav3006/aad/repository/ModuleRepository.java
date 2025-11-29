package com.farrnav3006.aad.repository;

import com.farrnav3006.aad.model.Module;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ModuleRepository {
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
    private final JdbcTemplate jdbcTemplate;

    public Module insert(Module module) {
        jdbcTemplate.update(SQL_INSERT, module.getCode(), module.getName(), module.getHours());
        return module;
    }

    public List<Module> findAll() {
        return jdbcTemplate.query(
                SQL_FINDALL,
                (rs, rowNum) -> new Module(
                        rs.getInt("id_modulo"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getInt("horas")
                )
        );
    }

    public Module findById(int id) {
        List<Module> modules = jdbcTemplate.query(
                SQL_FINDBYID,
                (rs, rowNum) -> new Module(
                        rs.getInt("id_modulo"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getInt("horas")
                ),
                id
        );
        return modules.isEmpty() ? null : modules.get(0);
    }

    public Module update(Module module) {
        int updated = jdbcTemplate.update(SQL_UPDATE, module.getCode(), module.getName(),
                module.getHours(), module.getId());
        if (updated == 0) {
            throw new RuntimeException("Module not found for update: id=" + module.getId());
        }
        return module;
    }

    public boolean delete(int id) {
        int deleted = jdbcTemplate.update(SQL_DELETE, id);
        return deleted > 0;
    }
}