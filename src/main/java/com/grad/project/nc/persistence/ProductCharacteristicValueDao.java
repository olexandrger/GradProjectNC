package com.grad.project.nc.persistence;

import com.grad.project.nc.model.ProductCharacteristicValue;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by Alex on 4/25/2017.
 */
@Repository
public class ProductCharacteristicValueDao implements CrudDao<ProductCharacteristicValue> {


    @Override
    public ProductCharacteristicValue add(ProductCharacteristicValue entity) {
        return null;
    }

    @Override
    public ProductCharacteristicValue update(ProductCharacteristicValue entity) {
        return null;
    }

    @Override
    public ProductCharacteristicValue find(long id) {
        return null;
    }

    @Override
    public Collection<ProductCharacteristicValue> findAll() {
        return null;
    }

    @Override
    public void delete(ProductCharacteristicValue entity) {

    }

    public class ProductCharacteristicValueRowMapper implements RowMapper<ProductCharacteristicValue> {
        @Override
        public ProductCharacteristicValue mapRow(ResultSet rs, int rowNum) throws SQLException {
            return null;
        }
    }
}
