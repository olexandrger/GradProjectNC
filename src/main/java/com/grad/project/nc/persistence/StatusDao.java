package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Status;
import com.grad.project.nc.persistence.mappers.StatusRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Collection;

@Repository
class StatusDao extends AbstractDao<Status> {

    @Autowired
    StatusDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Status add(Status entity) {

        KeyHolder keyHolder = executeInsert(connection -> {
            String statement = "INSERT INTO status (status_name, status_type_id) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, entity.getStatusName());
            preparedStatement.setLong(2, entity.getStatusTypeId());

            return preparedStatement;
        });

        entity.setStatusId(getLongValue(keyHolder, "status_id"));

        return entity;
    }

    @Override
    public Status update(Status entity) {

        executeUpdate(connection -> {
            String statement = "UPDATE status SET status_name=?, status_type_id=? WHERE status_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, entity.getStatusName());
            preparedStatement.setLong(2, entity.getStatusTypeId());
            preparedStatement.setLong(3, entity.getStatusId());

            return preparedStatement;
        });

        return entity;
    }

    @Override
    public Status find(long id) {

        return findOne(connection -> {
            String statement = "SELECT status_id, status_name, status_type_id FROM status WHERE status_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, id);

            return preparedStatement;
        }, new StatusRowMapper());

    }

    @Override
    public Collection<Status> findAll() {
        String statement = "SELECT status_id, status_name, status_type_id FROM status";

        return findMultiple(
                connection -> connection.prepareStatement(statement),
                new StatusRowMapper());
    }

    @Override
    public void delete(Status entity) {
        executeUpdate(connection -> {
            String statement = "DELETE FROM status WHERE status_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, entity.getStatusId());

            return preparedStatement;
        });
    }
}
