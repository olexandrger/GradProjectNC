package com.grad.project.nc.persistence;
import com.grad.project.nc.model.DataType;
import com.grad.project.nc.model.ProductCharacteristic;
import com.grad.project.nc.model.ProductType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@Slf4j
public class ProductCharacteristicDao implements CrudDao<ProductCharacteristic> {

    private JdbcTemplate jdbcTemplate;

//    @Autowired
    private DataTypeDao dataTypeDao;

    @Autowired
    public ProductCharacteristicDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setDataTypeDao(DataTypeDao dataTypeDao) {
        this.dataTypeDao = dataTypeDao;
    }

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
        parameters.put("data_type_id", productCharacteristic.getDataType().getDataTypeId());

        Number newId = insertProductQuery.executeAndReturnKey(parameters);
        productCharacteristic.setProductCharacteristicId(newId.longValue());

        return productCharacteristic;

    }

    @Transactional
    @Override
    public ProductCharacteristic find(Long id) {
        final String SELECT_QUERY = "SELECT product_characteristic_id" +
                ",product_type_id" +
                ",characteristic_name" +
                ",measure" +
                ",data_type_id" +
                " FROM product_characteristic " +
                "WHERE product_characteristic_id = ?";

        ProductCharacteristic productCharacteristic = null;
        try {
            productCharacteristic = jdbcTemplate.queryForObject(SELECT_QUERY,
                    new Object[]{id}, new ProductCharacteristicRowMapper());
        } catch (EmptyResultDataAccessException ex){

        }


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

        jdbcTemplate.update(UPDATE_QUERY, productCharacteristic.getProductTypeId()
                ,productCharacteristic.getCharacteristicName()
                ,productCharacteristic.getMeasure()
                ,productCharacteristic.getDataType().getDataTypeId()
                ,productCharacteristic.getProductCharacteristicId());
        return productCharacteristic;

    }

    @Transactional
    @Override
    public void delete(ProductCharacteristic entity) {

        final String DELETE_QUERY = "DELETE FROM product_characteristic WHERE product_characteristic_id = ?";

        jdbcTemplate.update(DELETE_QUERY,entity.getProductCharacteristicId());

    }


    @Override
    public List<ProductCharacteristic> findAll() {

        final String SELECT_QUERY = "SELECT product_characteristic_id" +
                ",product_type_id" +
                ",characteristic_name" +
                ",measure" +
                ",data_type_id" +
                " FROM product_characteristic ";

        return jdbcTemplate.query(SELECT_QUERY,new ProductCharacteristicRowMapper());
    }

    public List<ProductCharacteristic> findByProductTypeId(Long productTypeId){

        final String SELECT_QUERY = "SELECT pch.product_characteristic_id" +
                ",pch.product_type_id" +
                ",pch.characteristic_name" +
                ",pch.measure" +
                ",pch.data_type_id" +
                " FROM product_characteristic pch " +
                " WHERE pch.product_type_id = ?";

        return jdbcTemplate.query(SELECT_QUERY, new ProductCharacteristicRowMapper(), productTypeId);
    }


    public List<ProductCharacteristic> findByDataTypeId(Long id){

        final String QUERY = "SELECT product_characteristic_id" +
                ",product_type_id" +
                ", characteristic_name , measure , data_type_id " +
                "FROM product_characteristic WHERE data_type_id = ?" ;
        return jdbcTemplate.query(QUERY,new Object[]{id}, new ProductCharacteristicRowMapper());

    }

    public Collection<ProductCharacteristic> findCharacteristicsByProductType(ProductType entity) {
        final String QUERY = "SELECT product_characteristic_id" +
                ",product_type_id" +
                ", characteristic_name , measure , data_type_id " +
                "FROM product_characteristic WHERE product_type_id = ?" ;
        return jdbcTemplate.query(QUERY,new Object[]{entity.getProductTypeId()}, new ProductCharacteristicRowMapper());
    }

    public List<ProductCharacteristic> findByProductId(Long id) {
        return null;
    }


    private final class ProductCharacteristicRowMapper implements RowMapper<ProductCharacteristic> {
        @Override
        public ProductCharacteristic mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductCharacteristic productCharacteristic = new ProductCharacteristicProxy();

            productCharacteristic.setProductCharacteristicId(rs.getLong("product_characteristic_id"));
            productCharacteristic.setProductTypeId(rs.getLong("product_type_id"));
            productCharacteristic.setCharacteristicName(rs.getString("characteristic_name"));
            productCharacteristic.setMeasure(rs.getString("measure"));
//            productCharacteristic.setDataTypeId(rs.getLong("data_type_id"));


            return productCharacteristic;
        }
    }

    private class ProductCharacteristicProxy extends ProductCharacteristic {
        @Override
        public DataType getDataType() {
            if (super.getDataType() == null) {
                super.setDataType(dataTypeDao.getDataTypeByProductCharacteristic(this));
            }
            return super.getDataType();
        }
    }
}
