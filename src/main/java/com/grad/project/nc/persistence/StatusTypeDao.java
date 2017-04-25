package com.grad.project.nc.persistence;

import com.grad.project.nc.model.StatusType;
import com.grad.project.nc.persistence.mappers.StatusTypeRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;

@Repository
public class StatusTypeDao extends AbstractDao<StatusType> {

    @Autowired
    StatusTypeDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public StatusType add(StatusType entity) {
        KeyHolder keyHolder = executeInsert(connection -> {
            String statement = "INSERT INTO status_type (status_type_name) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, entity.getStatusTypeName());

            return preparedStatement;
        });

        entity.setStatusTypeId(((Number)keyHolder.getKeys().get("status_type_id")).longValue());

        return entity;
    }

    @Override
    public StatusType update(StatusType entity) {
        executeUpdate(connection -> {
            String statement = "UPDATE status_type SET status_type_name=? WHERE status_type_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, entity.getStatusTypeName());
            preparedStatement.setLong(2, entity.getStatusTypeId());

            return preparedStatement;
        });

        return entity;
    }

    @Override
    public StatusType find(long id) {
        return findOne(connection -> {
            String statement = "SELECT status_type_id, status_type_name FROM status_type WHERE status_type_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, id);

            return preparedStatement;
        }, new StatusTypeRowMapper());
    }

    @Override
    public Collection<StatusType> findAll() {
        final String statement = "SELECT status_type_id, status_type_name FROM status_type";
        return findMultiple(connection -> connection.prepareStatement(statement),
                new StatusTypeRowMapper());
    }

    @Override
    public void delete(StatusType entity) {
        executeUpdate(connection -> {
            final String statement = "DELETE FROM status_type WHERE status_type_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, entity.getStatusTypeId());

            return preparedStatement;
        });
    }
}
