package com.grad.project.nc.persistence;

import com.grad.project.nc.model.StatusType;
import com.grad.project.nc.persistence.exceptions.NonUniqueResultException;
import com.grad.project.nc.persistence.mappers.StatusTypeRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Repository
public class StatusTypeDao implements CrudDao<StatusType> {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public StatusTypeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public StatusType add(StatusType entity) {
        final String statement = "INSERT INTO status_type (status_type_name) VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, entity.getStatusTypeName());

            return preparedStatement;
        }, keyHolder);

        entity.setStatusTypeId(keyHolder.getKey().longValue());

        return entity;
    }

    @Override
    public StatusType update(StatusType entity) {
        final String statement = "UPDATE status_type SET status_type_name=? WHERE status_type_id=?";

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, entity.getStatusTypeName());
            preparedStatement.setLong(2, entity.getStatusTypeId());

            return preparedStatement;
        });

        return entity;
    }

    @Override
    public StatusType find(long id) {
        final String statement = "SELECT status_type_id, status_type_name FROM status_type WHERE status_type_id=?";

        List<StatusType> results = jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, id);

            return preparedStatement;
        }, new StatusTypeRowMapper());

        if (results.size() > 1) {
            throw new NonUniqueResultException();
        }

        if (results.size() == 0) {
            return null;
        }

        return results.get(0);
    }

    @Override
    public Collection<StatusType> findAll() {
        final String statement = "SELECT status_type_id, status_type_name FROM status_type";

        return jdbcTemplate.query(
                connection -> connection.prepareStatement(statement),
                new StatusTypeRowMapper());
    }

    @Override
    public void delete(StatusType entity) {
        final String statement = "DELETE FROM status_type WHERE status_type_id = ?";

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, entity.getStatusTypeId());

            return preparedStatement;
        });
    }
}
