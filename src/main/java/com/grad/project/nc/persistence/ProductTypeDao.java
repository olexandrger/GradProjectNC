package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductType;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;



@Repository
public class ProductTypeDao extends AbstractDao<ProductType> {



    @Autowired
    public ProductTypeDao(JdbcTemplate jdbcTemplate){
        super(jdbcTemplate);
    }


    @Transactional
    @Override
    public ProductType add(ProductType productType)  {


        //TODO add lists saving

        KeyHolder keyHolder = executeInsert(connection -> {
            String statement = "INSERT INTO \"product_type\" (product_type_name, product_type_description)" +
                    " VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, productType.getProductTypeName());
            preparedStatement.setString(2, productType.getProductTypeDescription());


            return preparedStatement;
        });

        return find(getLongValue(keyHolder, "product_type_id"));

    }

    @Override
    public ProductType find(long id) {

        return findOne(connection -> {
            String statement = "SELECT product_type_id,product_type_name,product_type_description FROM product_type WHERE product_type_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, id);

            return preparedStatement;
        }, new ProductTypeRowMapper());
    }

    @Transactional
    @Override
    public ProductType update(ProductType productType)  {


        executeUpdate(connection -> {
            String query = "UPDATE product_type SET product_type_name = ?" +
            ", product_type_description = ?" +
                    "WHERE product_type_id = ? ";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, productType.getProductTypeName());
            preparedStatement.setString(2, productType.getProductTypeDescription());
            preparedStatement.setLong(3, productType.getProductTypeId());

            return preparedStatement;
        });

        return productType;


    }


    @Override
    public void delete(ProductType entity)  {

        executeUpdate(connection -> {
            String statement = "DELETE FROM product_type WHERE product_type_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, entity.getProductTypeId());
            return preparedStatement;
        });

    }



    @Override
    public Collection<ProductType> findAll() {

        return findMultiple(connection -> {
            final String QUERY = "SELECT product_type_id,product_type_name,product_type_description FROM product_type";

            PreparedStatement preparedStatement = connection.prepareStatement(QUERY);

            return preparedStatement;

        }, new ProductTypeRowMapper());
    }
    public ProductType getProductTypeByProduct(Product product){

        return findOne(connection -> {
            final String SELECT_QUERY =
                    "SELECT pt.product_type_id, pt.product_type_name, pt.product_type_description FROM product_type pt " +
                            "WHERE pt.product_type_id = " +
                            "(SELECT " +
                            "p.product_type_id " +
                            "FROM product p " +
                            "WHERE p.product_id = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, product.getProductId());
            return preparedStatement;
        }, new ProductTypeRowMapper());


    }



    private static final class ProductTypeRowMapper implements RowMapper<ProductType> {
        @Override
        public ProductType mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductType productType = new ProductType();

            productType.setProductTypeId(rs.getLong("product_type_id"));
            productType.setProductTypeName(rs.getString("product_type_name"));
            productType.setProductTypeDescription(rs.getString("product_type_description"));

            return productType;
        }
    }


}
