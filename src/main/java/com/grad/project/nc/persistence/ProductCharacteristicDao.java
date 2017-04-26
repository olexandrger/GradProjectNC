package com.grad.project.nc.persistence;
import com.grad.project.nc.model.ProductCharacteristic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alex on 4/25/2017.
 */
@Repository
public class ProductCharacteristicDao implements CrudDao<ProductCharacteristic> {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Transactional
    @Override
    public ProductCharacteristic add(ProductCharacteristic productCharacteristic) {

        SimpleJdbcInsert insertProductQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product_characteristic")
                .usingGeneratedKeyColumns("product_characteristic_id");

        Map<String, Object> parameters = new HashMap<String, Object>(4);
        parameters.put("product_type_id", productCharacteristic.getProductTypeId());
        parameters.put("characteristic_name", productCharacteristic.getCharacteristicName());
        parameters.put("measure", productCharacteristic.getMeasure());
        parameters.put("data_type_id", productCharacteristic.getDataTypeId());

        Number newId = insertProductQuery.executeAndReturnKey(parameters);
        productCharacteristic.setProductCharacteristicId(newId.longValue());

        return productCharacteristic;

    }

    @Transactional
    @Override
    public ProductCharacteristic find(long id) {
        final String SELECT_QUERY = "SELECT product_characteristic_id" +
                ",product_type_id" +
                ",characteristic_name" +
                ",measure" +
                ",data_type_id" +
                " FROM product_characteristic " +
                "WHERE product_characteristic_id = ?";

        ProductCharacteristic productCharacteristic = jdbcTemplate.queryForObject(SELECT_QUERY,
                new Object[]{id}, new ProductCharacteristicRowMapper());

        return productCharacteristic;
    }

    @Transactional
    @Override
    public ProductCharacteristic update(ProductCharacteristic productCharacteristic) {
        final String UPDATE_QUERY = "UPDATE product_characteristic SET product_type_id = ?" +
                ", characteristic_name = ?" +
                ", measure = ?" +
                ", data_type_id = ? " +
                "WHERE product_characteristic_id = ? ";

        jdbcTemplate.update(UPDATE_QUERY, new Object[]{productCharacteristic.getProductTypeId()
                ,productCharacteristic.getCharacteristicName()
                ,productCharacteristic.getMeasure()
                ,productCharacteristic.getDataTypeId()
                ,productCharacteristic.getProductCharacteristicId()});
        return productCharacteristic;

    }

    @Transactional
    @Override
    public void delete(ProductCharacteristic entity) {

        final String DELETE_QUERY = "DELETE FROM product_characteristic WHERE product_characteristic_id = ?";

        jdbcTemplate.update(DELETE_QUERY,entity.getProductCharacteristicId());

    }


    @Override
    public Collection<ProductCharacteristic> findAll() {

        final String SELECT_QUERY = "SELECT product_characteristic_id" +
                ",product_type_id" +
                ",characteristic_name" +
                ",measure" +
                ",data_type_id" +
                " FROM product_characteristic ";

        return jdbcTemplate.query(SELECT_QUERY,new ProductCharacteristicRowMapper());
    }

    public static final class ProductCharacteristicRowMapper implements RowMapper<ProductCharacteristic> {
        @Override
        public ProductCharacteristic mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductCharacteristic productCharacteristic = new ProductCharacteristic();

            productCharacteristic.setProductCharacteristicId(rs.getLong("product_characteristic_id"));
            productCharacteristic.setProductTypeId(rs.getLong("product_type_id"));
            productCharacteristic.setCharacteristicName(rs.getString("characteristic_name"));
            productCharacteristic.setMeasure(rs.getString("measure"));
            productCharacteristic.setDataTypeId(rs.getLong("data_type_id"));


            return productCharacteristic;
        }
    }


}
