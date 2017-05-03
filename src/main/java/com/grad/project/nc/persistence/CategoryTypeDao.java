package com.grad.project.nc.persistence;

import com.grad.project.nc.model.CategoryType;
import com.grad.project.nc.model.Domain;
import com.grad.project.nc.persistence.mappers.CategoryTypeRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;

@Repository
public class CategoryTypeDao extends AbstractDao<CategoryType> {

    @Autowired
    CategoryTypeDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public CategoryType add(CategoryType entity) {
        KeyHolder keyHolder = executeInsert(connection -> {
            String statement = "INSERT INTO category_type (category_type_name) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, entity.getCategoryTypeName());

            return preparedStatement;
        });

        entity.setCategoryTypeId(((Number)keyHolder.getKeys().get("category_type_id")).longValue());

        return entity;
    }

    @Override
    public CategoryType update(CategoryType entity) {
        executeUpdate(connection -> {
            String statement = "UPDATE category_type SET category_type_name=? WHERE category_type_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, entity.getCategoryTypeName());
            preparedStatement.setLong(2, entity.getCategoryTypeId());

            return preparedStatement;
        });
        return entity;
    }

    @Override
    public CategoryType find(long id) {
        return findOne(connection -> {
            String statement = "SELECT category_type_id, category_type_name FROM category_type WHERE category_type_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, id);

            return preparedStatement;
        }, new CategoryTypeRowMapper());
    }

    @Override
    public Collection<CategoryType> findAll() {
        return findMultiple(connection -> {
            String statement = "SELECT category_type_id, category_type_name FROM category_type";
            return connection.prepareStatement(statement);
        }, new CategoryTypeRowMapper());
    }

    @Override
    public void delete(CategoryType entity) {
        executeUpdate(connection -> {
            final String statement = "DELETE FROM category WHERE category_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setLong(1, entity.getCategoryTypeId());
            return preparedStatement;
        });
    }

    public CategoryType getCategoryTypeOfDomain(Domain domain) {
        return findOne(connection -> {
            //language=GenericSQL
            final String SELECT_QUERY = "SELECT " +
                    "category_type_name, " +
                    "category_type_id  " +
                    "FROM category " +
                    "WHERE category_type_id = (" +
                    "SELECT d.domain_type_id " +
                    "FROM domain d " +
                    "WHERE d.domain_id = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, domain.getDomainId());
            return preparedStatement;
        }, new CategoryTypeRowMapper());
    }
}
