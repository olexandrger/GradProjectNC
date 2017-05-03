package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Complain;
import com.grad.project.nc.model.ProductInstance;
import com.grad.project.nc.model.ProductOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class ProductInstanceDao extends AbstractDao<ProductInstance>{

    @Autowired
    ProductInstanceDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public ProductInstance add(ProductInstance entity) {

        KeyHolder keyHolder = executeInsert(connection -> {
            String statement = "INSERT INTO product_instance (price_id, domain_id, status_id) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setLong(1, entity.getPriceId());
            preparedStatement.setLong(2, entity.getDomainId());
            preparedStatement.setLong(3, entity.getStatusId());

            return preparedStatement;
        });

        entity.setInstanceId(getLongValue(keyHolder, "instance_id"));

        return entity;
    }

    @Override
    public ProductInstance update(ProductInstance entity) {
        executeUpdate(connection -> {
            String statement = "UPDATE product_instance SET price_id = ?, domain_id = ?, status_id = ? WHERE instance_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, entity.getPriceId());
            preparedStatement.setLong(2, entity.getDomainId());
            preparedStatement.setLong(3, entity.getStatusId());
            preparedStatement.setLong(4, entity.getInstanceId());

            return preparedStatement;
        });

        return entity;
    }

    @Override
    public ProductInstance find(Long id) {
        return findOne(connection -> {
            String statement = "SELECT instance_id, price_id, domain_id, status_id FROM product_instance WHERE instance_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, id);

            return preparedStatement;
        }, new ProductInstanceRowMapper());

    }

    @Override
    public List<ProductInstance> findAll() {
        String findAllQuery = "SELECT \"instance_id\", \"price_id\", \"domain_id\", \"status_id\" " +
                "FROM \"product_instance\"";

        return query(findAllQuery, new ProductInstanceRowMapper());
    }

    @Override
    public void delete(ProductInstance entity) {
        executeUpdate(connection -> {
            String statement = "DELETE FROM product_instance WHERE instance_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, entity.getInstanceId());

            return preparedStatement;
        });
    }

    public ProductInstance findByProductOrder(ProductOrder productOrder){
        return null; //TODO find metod
    }

    public ProductInstance findByComplain(Complain complain){
        return null; //TODO find metod
    }


    private class ProductInstanceRowMapper implements RowMapper<ProductInstance> {
        @Override
        public ProductInstance mapRow(ResultSet resultSet, int i) throws SQLException {
            ProductInstance productInstance = new ProductInstance();

            productInstance.setInstanceId(resultSet.getLong("instance_id"));
            productInstance.setPriceId(resultSet.getLong("price_id"));
            productInstance.setDomainId(resultSet.getLong("domain_id"));
            productInstance.setStatusId(resultSet.getLong("status_id"));

            return productInstance;
        }
    }
}
