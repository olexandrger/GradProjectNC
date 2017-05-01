package com.grad.project.nc.persistence;

import com.grad.project.nc.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.*;


@Repository
public class ProductDao extends AbstractDao<Product> {
    @Autowired
    private ProductTypeDao productTypeDao;
    @Autowired
    private ProductCharacteristicValueDao productCharacteristicValueDao;
    @Autowired
    private ProductCharacteristicDao productCharacteristicDao;

    private ProductRowMapper mapper = new ProductRowMapper();


    @Autowired
    public ProductDao(JdbcTemplate jdbcTemplate/*, ProductTypeDao productTypeDao, ProductCharacteristicValueDao productCharacteristicValueDao*/){

        super(jdbcTemplate);
        //this.productTypeDao = productTypeDao;
        //this.productCharacteristicValueDao = productCharacteristicValueDao;
    }

    @Override
    public Product add(Product product) {


        //

        KeyHolder keyHolder = executeInsert(connection -> {
            String statement = "INSERT INTO \"product\" (product_name, product_description, product_type_id)" +
                    " VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setLong(3, product.getProductType().getProductTypeId());

            return preparedStatement;
        });

        product.setProductId(getLongValue(keyHolder, "product_id"));

        saveProductCharacteristicValues(product);

        return find(getLongValue(keyHolder, "product_id"));

    }

    @Transactional
    @Override
    public Product find(long id)  {

        return findOne(connection -> {
            String statement = "SELECT product_id,product_name,product_description FROM product WHERE product_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, id);

            return preparedStatement;
        }, mapper);

    }


    @Override
    public Product update(Product product) {


        executeUpdate(connection -> {
            String query = "UPDATE product SET " +
                    "product_name = ?" +
                    ", product_description = ?" +
                    ", product_type_id = ? " +
                    "WHERE product_id = ? ";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setLong(3, product.getProductType().getProductTypeId());
            preparedStatement.setLong(4, product.getProductId());


            return preparedStatement;
        });


        saveProductCharacteristicValues(product);

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
                   // ",product_type_id " +
                    "FROM product ";
            return connection.prepareStatement(statement);
        }, mapper);

    }

    private void deleteProductCharacteristicValues(Product product){

        executeUpdate(connection -> {
            String query = "DELETE FROM product_characteristic_value WHERE product_id =?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setLong(1, product.getProductId());

            return statement;
        });

    }
    private void saveProductCharacteristicValues(Product product){

        if(product.getProductCharacteristicValues() != null && product.getProductCharacteristicValues().size() > 0){

            deleteProductCharacteristicValues(product);

            executeUpdate(connection -> {
                String query = "INSERT INTO product_characteristic_value(product_id,product_characteristic_id, number_value , date_value , string_value  ) VALUES (?,?,?,?,?)";
                for (int i = 1; i < product.getProductCharacteristicValues().size(); i++) {
                    query += ",(?,?,?,?,?)";
                }

                PreparedStatement statement = connection.prepareStatement(query);

                int i = 1;
                for (ProductCharacteristicValue productCharacteristicValue : product.getProductCharacteristicValues()) {
                    statement.setLong(i, productCharacteristicValue.getProductId());
                    i++;
                    statement.setLong(i, productCharacteristicValue.getProductCharacteristicId());
                    i++;
                    statement.setDouble(i, productCharacteristicValue.getNumberValue());
                    i++;
                    statement.setTimestamp(i, Timestamp.valueOf(productCharacteristicValue.getDateValue()));
                    i++;
                    statement.setString(i, productCharacteristicValue.getStringValue());
                    i++;
                }
                return statement;
            });

        }else {
            deleteProductCharacteristicValues(product);
        }

    }

    public Collection<Product> findProductsByRegion(Region region){

        return findMultiple(connection -> {
            final String QUERY =
                    "SELECT p.product_id" +
                            ",p.product_name" +
                            ",p.product_description FROM product p INNER JOIN product_region_price prp " +
                            "ON p.product_id = prp.product_id " +
                            "WHERE prp.region_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
            preparedStatement.setLong(1, region.getRegionId());
            return preparedStatement;

        }, mapper);
    }

    //TODO Discuss if name of product unique or not
    @Transactional
    public Optional<Product> findByName(String name) {
        Product result = findOne(connection -> {
            String statement = "SELECT product_id,product_name,product_description" +
                    //",product_type_id " +
                    "FROM product WHERE product_name = ?";
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
                         //   ",product_type_id " +
                            "FROM product " +
                            "WHERE product_type_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
            preparedStatement.setLong(1, productType.getProductTypeId());
            return preparedStatement;

        }, mapper);

    }

    public Product getProductByProductRegionPrise(ProductRegionPrice productRegionPrice) {
        return findOne(connection -> {
            final String SELECT_QUERY =
                    "SELECT " +
                            "pr.product_id, " +
                            "pr.product_name, " +
                            "pr.product_description, " +
                            "pr.product_type_id " +
                            "FROM product pr " +
                            "WHERE pr.product_id = " +
                            "(SELECT " +
                            "prp.product_id " +
                            "FROM product_region_price prp " +
                            "WHERE prp.price_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, productRegionPrice.getPriceId());
            return preparedStatement;

        }, mapper);
    }

    public static final class ProductRowMapper implements RowMapper<Product> {

        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();

            product.setProductId(rs.getLong("product_id"));
            product.setName(rs.getString("product_name"));
            product.setDescription(rs.getString("product_description"));

            return product;
        }
    }

    private class ProductProxy extends Product{
        @Override
        public ProductType getProductType() {

            if (super.getProductType() == null) {
                super.setProductType(productTypeDao.getProductTypeByProduct(this));
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

        public List<ProductCharacteristic> getProductCharacteristics() {
            if (super.getProductCharacteristics() == null){
                super.setProductCharacteristics(new LinkedList<>(productCharacteristicDao.findByProductId(this.getProductId())));
            }

            return super.getProductCharacteristics();
        }


    }


}
