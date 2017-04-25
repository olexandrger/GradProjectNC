package com.grad.project.nc.persistence;

import com.grad.project.nc.model.DataType;
import com.grad.project.nc.model.ProductType;
import com.grad.project.nc.persistence.mappers.DataTypeRowMapper;
import com.grad.project.nc.persistence.mappers.ProductTypeRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alex on 4/25/2017.
 */
@Component
public class DataTypeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Transactional
    public void insertDataType(DataType dataType) {

        SimpleJdbcInsert insertProductQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("data_type")
                .usingGeneratedKeyColumns("data_type_id");

        Map<String, Object> parameters = new HashMap<String, Object>(1);
        parameters.put("data_type", dataType.getDataType());

        Number newId = insertProductQuery.executeAndReturnKey(parameters);
        dataType.setDataTypeId(newId.intValue());

    }

    @Transactional
    public DataType readDataTypeById(int id) {
        final String SELECT_QUERY = "SELECT * FROM data_type where data_type_id = ?";

        DataType dataType = jdbcTemplate.queryForObject(SELECT_QUERY,
                new Object[]{id}, new DataTypeRowMapper());

        return dataType;
    }

    @Transactional
    public void updateDataType(DataType dataType) {
        final String UPDATE_QUERY = "UPDATE data_type SET data_type = ?" +
                "WHERE data_type_id = ? ";

        jdbcTemplate.update(UPDATE_QUERY, new Object[]{dataType.getDataType()});


    }

    @Transactional
    public void deleteDataTypeById(int id) {

        final String DELETE_QUERY = "DELETE FROM data_type WHERE data_type_id = ?";

        jdbcTemplate.update(DELETE_QUERY,id);

    }
}
