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
import java.util.Collection;
import java.util.List;

public abstract class AbstractDao<T> implements CrudDao<T>{

    private final JdbcTemplate jdbcTemplate;

    protected AbstractDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected Long getLongValue(KeyHolder keyHolder, String keyName) {
        return ((Number)keyHolder.getKeys().get(keyName)).longValue();
    }

    protected KeyHolder executeInsert(PreparedStatementCreator statementCreator) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(statementCreator, keyHolder);
        return keyHolder;
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

    protected void executeUpdate(PreparedStatementCreator statementCreator) {
        jdbcTemplate.update(statementCreator);
    }

    protected int executeUpdate(String sql, Object... params) {
        return jdbcTemplate.update(sql, params);
    }

    protected <E> E findOne(PreparedStatementCreator statementCreator, RowMapper<E> mapper) {
        List<E> result = jdbcTemplate.query(statementCreator, mapper);

        if (result.size() > 1) {
            throw new NonUniqueResultException();
        }

        if (result.size() == 0) {
            return null;
        }

        return result.get(0);
    }

    protected T findOne(String query, RowMapper<T> mapper, Object... params) {
        List<T> result = jdbcTemplate.query(query, mapper, params);

        if (result.size() > 1) {
            throw new NonUniqueResultException();
        }
        if (result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    protected <E> Collection<E> findMultiple(PreparedStatementCreator statementCreator, RowMapper<E> mapper) {
        return jdbcTemplate.query(statementCreator, mapper);
    }

    protected List<T> query(String sql, RowMapper<T> rowMapper) {
        return jdbcTemplate.query(sql, rowMapper);
    }

    protected List<T> query(String sql, RowMapper<T> rowMapper, Object... params) {
        return jdbcTemplate.query(sql, rowMapper, params);
    }

    protected int[] batchUpdate(String sql, List<Object[]> batchArgs) {
        return jdbcTemplate.batchUpdate(sql, batchArgs);
    }
}
