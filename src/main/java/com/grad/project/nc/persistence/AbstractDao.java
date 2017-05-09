package com.grad.project.nc.persistence;

import com.grad.project.nc.persistence.exceptions.NonUniqueResultException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractDao {

    private final JdbcTemplate jdbcTemplate;

    public AbstractDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected Long executeInsertWithId(String insertQuery, String pkColumnName, Object... params) {
        final PreparedStatementCreator psc = connection -> {
            final PreparedStatement ps = connection.prepareStatement(insertQuery,
                    Statement.RETURN_GENERATED_KEYS);
            if (params != null && params.length > 0) {
                new ArgumentPreparedStatementSetter(params).setValues(ps);
            }
            return ps;
        };

        final KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(psc, holder);

        return ((Number) holder.getKeys().get(pkColumnName)).longValue();
    }

    protected int executeUpdate(String sql, Object... params) {
        return jdbcTemplate.update(sql, params);
    }

    protected <E> E findOne(String query, RowMapper<E> mapper, Object... params) {
        List<E> result = jdbcTemplate.query(query, mapper, params);

        if (result.size() > 1) {
            throw new NonUniqueResultException();
        }
        if (result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    protected <E> List<E> findMultiple(String sql, RowMapper<E> rowMapper) {
        return jdbcTemplate.query(sql, rowMapper);
    }

    protected <E> List<E> findMultiple(String sql, RowMapper<E> rowMapper, Object... params) {
        return jdbcTemplate.query(sql, rowMapper, params);
    }

    protected <E> List<E> findMultiplePage(String sql, RowMapper<E> rowMapper, Long size, Long offset) {
        return jdbcTemplate.query(sql + " LIMIT ? OFFSET ?", rowMapper, size, offset);
    }

    protected <E> List<E> findMultiplePage(String sql, RowMapper<E> rowMapper, Long size, Long offset, Object... params) {
        Object[] newParams = Arrays.copyOf(params, params.length + 2);
        newParams[params.length] = size;
        newParams[params.length + 1] = offset;
        return jdbcTemplate.query(sql + " LIMIT ? OFFSET ?", rowMapper, newParams);
    }

    protected int[] batchUpdate(String sql, List<Object[]> batchArgs) {
        return jdbcTemplate.batchUpdate(sql, batchArgs);
    }
}
