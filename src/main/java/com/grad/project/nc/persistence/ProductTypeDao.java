package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductCharacteristic;
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
import java.util.LinkedList;
import java.util.List;


@Repository
public class ProductTypeDao extends AbstractDao<ProductType> {

    @Autowired
    private ProductCharacteristicDao productCharacteristicDao;


    @Autowired
    public ProductTypeDao(JdbcTemplate jdbcTemplate){
        super(jdbcTemplate);
    }

    @Override
    public ProductType add(ProductType productType)  {

        saveProductCharacteristic(productType);

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

    @Override
    public ProductType update(ProductType productType)  {
        saveProductCharacteristic(productType);

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

        Collection<ProductCharacteristic> characteristics = productCharacteristicDao.findCharacteristicsByProductType(entity);

        characteristics.forEach(item -> {
            productCharacteristicDao.delete(item);
        });

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
                            "INNER JOIN product p ON p.product_type_id=pt.product_type_id WHERE p.product_id=?";
//                    "SELECT pt.product_type_id, pt.product_type_name, pt.product_type_description FROM product_type pt " +
//                            "WHERE pt.product_type_id = " +
//                            "(SELECT " +
//                            "p.product_type_id " +
//                            "FROM product p " +
//                            "WHERE p.product_id = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, product.getProductId());
            return preparedStatement;
        }, new ProductTypeRowMapper());


    }

    private void deleteProductCharacteristic(ProductType productType) {
        executeUpdate(connection -> {
            String query =
                    "DELETE  " +
                            "FROM product_characteristic " +
                            "WHERE product_type_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, productType.getProductTypeId());
            return preparedStatement;
        });
    }

    @Transactional
    private void saveProductCharacteristic(ProductType productType) {

        if (productType.getProductCharacteristics() != null && productType.getProductCharacteristics().size() > 0) {
            deleteProductCharacteristic(productType);
            for (ProductCharacteristic productCharacteristic : productType.getProductCharacteristics()) {
                productCharacteristicDao.add(productCharacteristic);
            }
        } else {
            deleteProductCharacteristic(productType);
        }
    }

    private class ProductTypeProxy extends ProductType{

        @Override
        public List<ProductCharacteristic> getProductCharacteristics() {
            if (super.getProductCharacteristics() == null){
                super.setProductCharacteristics(new LinkedList<>(productCharacteristicDao.findCharacteristicsByProductType(this)));
            }

            return super.getProductCharacteristics();
        }
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
