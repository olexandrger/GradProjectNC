package com.grad.project.nc.persistence;

import com.grad.project.nc.persistence.exceptions.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

@Repository
abstract class AbstractDao<T> implements CrudDao<T> {

    private final JdbcTemplate jdbcTemplate;

    AbstractDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    Long getLongValue(KeyHolder keyHolder, String keyName) {
        return ((Number)keyHolder.getKeys().get(keyName)).longValue();
    }

    KeyHolder executeInsert(PreparedStatementCreator statementCreator) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(statementCreator, keyHolder);
        return keyHolder;
    }

    void executeUpdate(PreparedStatementCreator statementCreator) {
        jdbcTemplate.update(statementCreator);
    }

    T findOne(PreparedStatementCreator statementCreator, RowMapper<T> mapper) {
        List<T> results = jdbcTemplate.query(statementCreator, mapper);

        if (results.size() > 1) {
            throw new NonUniqueResultException();
        }

        if (results.size() == 0) {
            return null;
        }

        return results.get(0);

    }

    <E> Collection<E> findMultiple(PreparedStatementCreator statementCreator, RowMapper<E> mapper) {
        return jdbcTemplate.query(statementCreator, mapper);
    }
}
