package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductCharacteristicValue;
import com.grad.project.nc.model.ProductType;
import com.grad.project.nc.model.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


@Repository
public class ProductDao extends AbstractDao<Product> {

    private ProductTypeDao productTypeDao;
    private ProductCharacteristicValueDao productCharacteristicValueDao;


    @Autowired
    public ProductDao(JdbcTemplate jdbcTemplate, ProductTypeDao productTypeDao, ProductCharacteristicValueDao productCharacteristicValueDao){

        super(jdbcTemplate);
        this.productTypeDao = productTypeDao;
        this.productCharacteristicValueDao = productCharacteristicValueDao;
    }

    @Override
    public Product add(Product product) {


        //TODO add lists saving

        KeyHolder keyHolder = executeInsert(connection -> {
            String statement = "INSERT INTO \"product\" (product_name, product_description, product_type_id)" +
                    " VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setLong(3, product.getProductTypeId());

            return preparedStatement;
        });

        return find(getLongValue(keyHolder, "user_id"));

    }

    @Transactional
    @Override
    public Product find(long id)  {

        return findOne(connection -> {
            String statement = "SELECT product_id,product_name,product_description,product_type_id FROM product WHERE product_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, id);

            return preparedStatement;
        }, new ProductRowMapper());

    }


    @Override
    public Product update(Product product) {


        executeUpdate(connection -> {
            String query = "UPDATE product SET product_name = ?" +
                    ", product_description = ?" +
                    ", product_type_id = ? " +
                    "WHERE product_id = ? ";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setLong(3, product.getProductTypeId());
            preparedStatement.setLong(4, product.getProductId());


            return preparedStatement;
        });

        //TODO add object fields saving

        return product;
    }

    @Transactional
    @Override
    public void delete(Product entity){

        executeUpdate(connection -> {
            String statement = "DELETE FROM product WHERE product_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, entity.getProductId());
            return preparedStatement;
        });

    }


    @Override
    public Collection<Product> findAll() {

        return findMultiple(connection -> {
            String statement = "SELECT product_id" +
                    ",product_name" +
                    ",product_description" +
                    ",product_type_id " +
                    "FROM product ";
            return connection.prepareStatement(statement);
        }, new ProductRowMapper());

    }

    public Collection<Product> findProductsByRegion(Region region){

        return findMultiple(connection -> {
            final String QUERY =
                    "SELECT p.product_id" +
                            ",p.product_name" +
                            ",p.product_description" +
                            ",p.product_type_id " +
                            "FROM product p" +
                            "INNER JOIN product_region_price prp " +
                            "ON p.product_id = prp.product_id " +
                            "WHERE prp.region_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
            preparedStatement.setLong(1, region.getRegionId());
            return preparedStatement;

        }, new ProductRowMapper());
    }

    //TODO Discuss if name of product unique or not
    @Transactional
    public Optional<Product> findByName(String name) {
        Product result = findOne(connection -> {
            String statement = "SELECT product_id,product_name,product_description,product_type_id FROM product WHERE product_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, name);

            return preparedStatement;
        }, new ProductRowMapper());

        return Optional.of(result);
    }
    @Transactional
    public Collection<Product> findProductsByType(ProductType productType){

        return findMultiple(connection -> {
            final String QUERY =
                    "SELECT product_id" +
                            ",product_name" +
                            ",product_description" +
                            ",product_type_id " +
                            "FROM product " +
                            "WHERE product_type_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
            preparedStatement.setLong(1, productType.getProductTypeId());
            return preparedStatement;

        }, new ProductRowMapper());

    }

    public static final class ProductRowMapper implements RowMapper<Product> {

        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();

            product.setProductId(rs.getLong("product_id"));
            product.setName(rs.getString("product_name"));
            product.setDescription(rs.getString("product_description"));
            product.setProductTypeId(rs.getLong("product_type_id"));

            return product;
        }
    }

    private class ProductProxy extends Product{
        @Override
        public ProductType getProductType() {

            if (super.getProductType() == null) {
                super.setProductType(productTypeDao.find(this.getProductTypeId()));
            }

            return super.getProductType();
        }

        @Override
        public List<ProductCharacteristicValue> getProductCharacteristicValues() {
            if (super.getProductCharacteristicValues() == null){
                super.setProductCharacteristicValues(productCharacteristicValueDao.findByProductId(this.getProductId()));
            }

            return super.getProductCharacteristicValues();
        }
    }


}
