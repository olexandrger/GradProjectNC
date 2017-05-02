package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.Complain;
import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductOrder;
import com.grad.project.nc.persistence.mappers.CategoryRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;

@Repository
public class CategoryDao extends AbstractDao<Category> {
    @Autowired
    public CategoryDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Category add(Category entity) {
        KeyHolder keyHolder = executeInsert(connection -> {
            String statement = "INSERT INTO category (category_name, category_type_id) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, entity.getCategoryName());
            preparedStatement.setLong(2, entity.getCategoryTypeId());

            return preparedStatement;
        });

        entity.setCategoryId(((Number)keyHolder.getKeys().get("category_id")).longValue());

        return entity;
    }

    @Override
    public Category update(Category entity) {
        executeUpdate(connection -> {
            String statement = "UPDATE category SET category_name=?, category_type_id=? WHERE category_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, entity.getCategoryName());
            preparedStatement.setLong(2, entity.getCategoryTypeId());
            preparedStatement.setLong(3, entity.getCategoryId());

            return preparedStatement;
        });

        return entity;
    }

    @Override
    public Category find(long id) {
        return findOne(connection -> {
            String statement = "SELECT category_id, category_name, category_type_id FROM category WHERE category_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, id);

            return preparedStatement;
        }, new CategoryRowMapper());
    }

    @Override
    public Collection<Category> findAll() {
        return findMultiple(connection -> {
            String statement = "SELECT category_id, category_name, category_type_id FROM category";
            return connection.prepareStatement(statement);
        }, new CategoryRowMapper());
    }

    @Override
    public void delete(Category entity) {
        executeUpdate(connection -> {
            String statement = "DELETE FROM status WHERE status_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, entity.getCategoryId());

            return preparedStatement;
        });
    }

    public Category findAimByProductOrder(ProductOrder productOrder){
        return null; //TODO find method
    }
    public Category findOrderStatusByProductOrder(ProductOrder productOrder){
        return null; //TODO find method
    }

    public Category findComplainStatusByComplain(Complain complain){
        return null; //TODO find method
    }

    public Category findComplainReasonByComplain(Complain complain){
        return null; //TODO find method
    }
    public Category findProductStatusByProduct(Product product){


        return null; //TODO find method
    }
}
