package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.Complain;
import com.grad.project.nc.model.ProductInstance;
import com.grad.project.nc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Collection;

@Repository
public class ComplainDao extends AbstractDao<Complain> {

    ComplainRowMapper mapper = new ComplainRowMapper();

    @Autowired
    ComplainDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Complain add(Complain entity) {

        KeyHolder keyHolder = executeInsert(connection -> {
            String statement =
                    "INSERT " +
                            "INTO complain (" +
                            "user_id, " +
                            "product_instance_id, " +
                            "complain_title, " +
                            "content, " +
                            "status_id, " +
                            "responsible_id, " +
                            "response, " +
                            "open_date, " +
                            "close_date, " +
                            "complain_reason_id) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setLong(1, entity.getUser().getUserId());
            preparedStatement.setLong(2, entity.getProductInstance().getInstanceId());
            preparedStatement.setString(3, entity.getComplainTitle());
            preparedStatement.setString(4, entity.getContent());
            preparedStatement.setLong(5, entity.getStatus().getCategoryId());
            preparedStatement.setLong(6, entity.getResponsible().getUserId());
            preparedStatement.setString(7, entity.getResponse());
            preparedStatement.setTimestamp(8, Timestamp.valueOf(entity.getOpenDate()));
            preparedStatement.setTimestamp(9, Timestamp.valueOf(entity.getOpenDate()));
            preparedStatement.setLong(10, entity.getComplainReason().getCategoryId());

            return preparedStatement;
        });

//        entity.setComplainId(getLongValue(keyHolder, "complain_id"));

        return find(getLongValue(keyHolder, "complain_id"));
    }

    @Override
    public Complain update(Complain entity) {

        executeUpdate(connection -> {
            String statement =
                    "UPDATE complain " +
                            "SET " +
                            "user_id=?, " +
                            "product_instance_id=?, " +
                            "complain_title=?, " +
                            "content=?, " +
                            "status_id=?, " +
                            "responsible_id=?, " +
                            "response=?, " +
                            "open_date=?, " +
                            "close_date=?, " +
                            "complain_reason_id=? " +
                            "WHERE complain_id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, entity.getUser().getUserId());
            preparedStatement.setLong(2, entity.getProductInstance().getInstanceId());
            preparedStatement.setString(3, entity.getComplainTitle());
            preparedStatement.setString(4, entity.getContent());
            preparedStatement.setLong(5, entity.getStatus().getCategoryId());
            preparedStatement.setLong(6, entity.getResponsible().getUserId());
            preparedStatement.setString(7, entity.getResponse());
            preparedStatement.setTimestamp(8, Timestamp.valueOf(entity.getOpenDate()));
            preparedStatement.setTimestamp(9, Timestamp.valueOf(entity.getOpenDate()));
            preparedStatement.setLong(10, entity.getComplainReason().getCategoryId());
            preparedStatement.setLong(11, entity.getComplainId());


            return preparedStatement;
        });

        return entity;
    }

    @Override
    public Complain find(long id) {

        return findOne(connection -> {
            String statement =
                    "SELECT " +
                            "complain_id, " +
                            "complain_title, " +
                            "content, " +
                            "response, " +
                            "open_date, " +
                            "close_date, " +
                            "FROM complain " +
                            "WHERE complain_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, id);

            return preparedStatement;
        }, mapper);
    }

    @Override
    public Collection<Complain> findAll() {

        return findMultiple(connection -> {
            String statement =
                    "SELECT " +
                            "complain_id, " +
                            "complain_title, " +
                            "content, " +
                            "response, " +
                            "open_date, " +
                            "close_date, " +
                            "FROM complain";

            return connection.prepareStatement(statement);
        }, mapper);
    }

    @Override
    public void delete(Complain entity) {
        executeUpdate(connection -> {
            String statement =
                    "DELETE " +
                            "FROM complain " +
                            "WHERE complain_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, entity.getComplainId());

            return preparedStatement;
        });
    }

    Collection<Complain> getComplainsByUser(User user) {
        return findMultiple(connection -> {
            String query =
                    "SELECT " +
                            "complain_id, " +
                            "complain_title, " +
                            "content, " +
                            "response, " +
                            "open_date, " +
                            "close_date, " +
                            "FROM complain " +
                            "INNER JOIN \"user\" " +
                            "ON complain.user_id=\"user\".user_id " +
                            "WHERE \"user\".user_id=?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setLong(1, user.getUserId());

            return statement;
        }, mapper);
    }

    private class ComplainProxy extends Complain {

        @Autowired
        UserDao userDao;
        @Autowired
        ProductInstanceDao productInstanceDao;
        @Autowired
        CategoryDao categoryDao;

        //TODO override metods
        @Override
        public User getUser() {
            if(super.getUser() == null){
                super.setUser(userDao.findUserComplain(this));
            }
            return super.getUser();
        }

        @Override
        public ProductInstance getProductInstance() {
            if(super.getProductInstance() == null){
                super.setProductInstance(productInstanceDao.findByComplain(this));
            }
            return  super.getProductInstance();
        }

        @Override
        public Category getStatus() {
            if(super.getStatus() == null){
                super.setStatus(categoryDao.findComplainStatusByComplain(this));
            }
            return super.getStatus();
        }

        @Override
        public User getResponsible() {
            if(super.getResponsible() == null){
                super.setResponsible(userDao.findResponsibleByComplain(this));
            }
            return super.getResponsible();
        }

        @Override
        public Category getComplainReason() {
            if(super.getComplainReason() == null){
                super.setComplainReason(categoryDao.findComplainReasonByComplain(this));
            }
            return super.getComplainReason();
        }
    }

    private class ComplainRowMapper implements RowMapper<Complain> {

        @Override
        public Complain mapRow(ResultSet resultSet, int i) throws SQLException {
            Complain complain = new ComplainProxy();


            complain.setComplainId(resultSet.getLong("complain_id"));
//            complain.setUserId(resultSet.getLong("user_id"));
//            complain.setProductInstanceId(resultSet.getLong("product_instance_id"));
            complain.setComplainTitle(resultSet.getString("complain_title"));
            complain.setContent(resultSet.getString("content"));
//            complain.setStatusId(resultSet.getLong("status_id"));
//            complain.setResponsibleId(resultSet.getLong("responsible_id"));
            complain.setResponse(resultSet.getString("response"));
//            complain.setComplainReasonId(resultSet.getLong("complain_reason_id"));
            complain.setOpenDate(resultSet.getTimestamp("open_date").toLocalDateTime());

            if (resultSet.getTimestamp("close_date") != null) {
                complain.setCloseDate(resultSet.getTimestamp("close_date").toLocalDateTime());
            }

            return complain;
        }
    }
}