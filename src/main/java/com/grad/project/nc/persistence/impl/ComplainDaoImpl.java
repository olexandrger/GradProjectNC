package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.Complain;
import com.grad.project.nc.model.proxy.ComplainProxy;
import com.grad.project.nc.persistence.AbstractDao;
import com.grad.project.nc.persistence.ComplainDao;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Repository
public class ComplainDaoImpl extends AbstractDao implements ComplainDao {

    private static final String PK_COLUMN_NAME = "complain_id";

    private final ObjectFactory<ComplainProxy> proxyFactory;

    @Autowired
    public ComplainDaoImpl(JdbcTemplate jdbcTemplate, ObjectFactory<ComplainProxy> proxyFactory) {
        super(jdbcTemplate);
        this.proxyFactory = proxyFactory;
    }

    @Override
    public Complain add(Complain complain) {
        String insertQuery = "INSERT INTO \"complain\" (\"user_id\", \"product_instance_id\", \"complain_title\", " +
                "\"content\", \"status_id\", \"responsible_id\", \"response\", \"open_date\", \"close_date\", " +
                "\"complain_reason_id\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Long complainId = executeInsertWithId(
                insertQuery,
                PK_COLUMN_NAME,
                complain.getUser().getUserId(),
                (complain.getProductInstance() == null) ? null : complain.getProductInstance().getInstanceId(),
                complain.getComplainTitle(),
                complain.getContent(),
                complain.getStatus().getCategoryId(),
                (complain.getResponsible() == null) ? (null) : (complain.getResponsible().getUserId()),
                complain.getResponse(),
                complain.getOpenDate(),
                complain.getCloseDate(),
                complain.getComplainReason().getCategoryId());

        complain.setComplainId(complainId);

        return complain;
    }

    @Override
    public Complain update(Complain complain) {
        String updateQuery = "UPDATE \"complain\" SET \"user_id\"=?, \"product_instance_id\"=?, " +
                "\"complain_title\"=?, \"content\"=?, \"status_id\"=?, \"responsible_id\"=?, \"response\"=?, " +
                "\"open_date\"=?, \"close_date\"=?, \"complain_reason_id\"=? WHERE \"complain_id\"=?";


        executeUpdate(updateQuery, complain.getUser().getUserId(),
                (complain.getProductInstance() == null) ? null : complain.getProductInstance().getInstanceId(),
                complain.getComplainTitle(),
                complain.getContent(),
                complain.getStatus().getCategoryId(),
                (complain.getResponsible() == null) ? (null) : (complain.getResponsible().getUserId()),
                complain.getResponse(),
                complain.getOpenDate(),
                complain.getCloseDate(),
                complain.getComplainReason().getCategoryId(),
                complain.getComplainId());

        return complain;
    }

    @Override
    public Complain find(Long id) {
        String findOneQuery = "SELECT \"complain_id\", \"user_id\", \"product_instance_id\", " +
                "\"complain_title\", \"content\", \"status_id\", \"responsible_id\", \"response\", " +
                "\"open_date\", \"close_date\", \"complain_reason_id\" FROM \"complain\" " +
                "WHERE \"complain_id\" = ?";

        return findOne(findOneQuery, new ComplainRowMapper(), id);
    }

    @Override
    public List<Complain> findAll() {
        String findAllQuery = "SELECT \"complain_id\", \"user_id\", \"product_instance_id\", " +
                "\"complain_title\", \"content\", \"status_id\", \"responsible_id\", \"response\", " +
                "\"open_date\", \"close_date\", \"complain_reason_id\" FROM \"complain\" " +
                "ORDER BY close_date DESC NULLS FIRST, open_date DESC";

        return findMultiple(findAllQuery, new ComplainRowMapper());
    }

    @Override
    public List<Complain> findAll(long size, long offset) {
        String findAllQuery = "SELECT \"complain_id\", \"user_id\", \"product_instance_id\", " +
                "\"complain_title\", \"content\", \"status_id\", \"responsible_id\", \"response\", " +
                "\"open_date\", \"close_date\", \"complain_reason_id\" FROM \"complain\" " +
                " ORDER BY close_date DESC NULLS FIRST, open_date DESC";
        return findMultiplePage(findAllQuery, new ComplainRowMapper(), size, offset);
    }

    @Override
    public void delete(Long id) {
        String deleteQuery = "DELETE FROM \"complain\" WHERE \"complain_id\" = ?";

        executeUpdate(deleteQuery, id);
    }

    @Override
    public List<Complain> findByUserId(Long userId) {
        String query = "SELECT c.\"complain_id\", c.\"user_id\", c.\"product_instance_id\", " +
                "c.\"complain_title\", c.\"content\", c.\"status_id\", c.\"responsible_id\", c.\"response\", " +
                "c.\"open_date\", c.\"close_date\", c.\"complain_reason_id\" " +
                "FROM \"complain\" c " +
                "INNER JOIN \"user\" u " +
                "ON c.\"user_id\" = u.\"user_id\" " +
                "WHERE u.\"user_id\" = ?";

        return findMultiple(query, new ComplainRowMapper(), userId);
    }

    @Override
    public List<Complain> findByUserId(Long userId, long size, long offset) {
        String query = "SELECT c.\"complain_id\", c.\"user_id\", c.\"product_instance_id\", " +
                "c.\"complain_title\", c.\"content\", c.\"status_id\", c.\"responsible_id\", c.\"response\", " +
                "c.\"open_date\", c.\"close_date\", c.\"complain_reason_id\" " +
                "FROM \"complain\" c " +
                "INNER JOIN \"user\" u " +
                "ON c.\"user_id\" = u.\"user_id\" " +
                "WHERE u.\"user_id\" = ?";

        return findMultiplePage(query, new ComplainRowMapper(), size, offset, userId);
    }

    @Override
    public List<Complain> findByInstanceId(Long instanceId, long size, long offset) {
            String findAllQuery = "SELECT \"complain_id\", \"user_id\", \"product_instance_id\", " +
                    "\"complain_title\", \"content\", \"status_id\", \"responsible_id\", \"response\", " +
                    "\"open_date\", \"close_date\", \"complain_reason_id\" FROM \"complain\" " +
                    "WHERE \"product_instance_id\" = ? " +
                    "ORDER BY close_date DESC NULLS FIRST, open_date DESC";
            return findMultiplePage(findAllQuery, new ComplainRowMapper(), size, offset, instanceId);
    }

    private class ComplainRowMapper implements RowMapper<Complain> {

        @Override
        public Complain mapRow(ResultSet resultSet, int i) throws SQLException {
            ComplainProxy complain = proxyFactory.getObject();

            complain.setComplainId(resultSet.getLong("complain_id"));
            complain.setUserId(resultSet.getLong("user_id"));
            complain.setProductInstanceId(resultSet.getLong("product_instance_id"));
            complain.setComplainTitle(resultSet.getString("complain_title"));
            complain.setContent(resultSet.getString("content"));
            complain.setStatusId(resultSet.getLong("status_id"));
            complain.setResponsibleId(resultSet.getLong("responsible_id"));
            complain.setResponse(resultSet.getString("response"));
            complain.setComplainReasonId(resultSet.getLong("complain_reason_id"));
            complain.setOpenDate(OffsetDateTime
                    .ofInstant(resultSet.getTimestamp("open_date").toInstant(), ZoneId.systemDefault()));

            if (resultSet.getTimestamp("close_date") != null) {
                complain.setCloseDate(OffsetDateTime
                        .ofInstant(resultSet.getTimestamp("close_date").toInstant(), ZoneId.systemDefault()));
            }

            return complain;
        }
    }
}
