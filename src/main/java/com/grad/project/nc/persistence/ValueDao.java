package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Value;
import com.grad.project.nc.persistence.mappers.ValueRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alex on 4/25/2017.
 * !!!!
 */
@Component
public class ValueDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Transactional
    public void insertValue(Value value) {

        SimpleJdbcInsert insertProductQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("value")
                .usingGeneratedKeyColumns("value_id");

        Map<String, Object> parameters = new HashMap<String, Object>(4);
        parameters.put("product_characteristic_id", value.getProductCharacteristicId());
        parameters.put("number_value", value.getNumberValue());


        parameters.put("date_value", Timestamp.valueOf(value.getDateValue()));
        parameters.put("string_value", value.getStringValue());

        Number newId = insertProductQuery.executeAndReturnKey(parameters);
        value.setValueId(newId.intValue());

    }

    @Transactional
    public Value readValueById(int id) {
        final String SELECT_QUERY = "SELECT * FROM value where value_id = ?";

        Value value = jdbcTemplate.queryForObject(SELECT_QUERY,
                new Object[]{id}, new ValueRowMapper());

        return value;
    }

    @Transactional
    public void updateValue(Value value) {
        final String UPDATE_QUERY = "UPDATE value SET product_characteristic_id = ?" +
                ", number_value = ?" +
                ", date_value = ?" +
                ", string_value = ? " +
                "WHERE value_id = ? ";

        jdbcTemplate.update(UPDATE_QUERY, new Object[]{value.getProductCharacteristicId()
                ,value.getNumberValue()
                ,Timestamp.valueOf(value.getDateValue())
                ,value.getStringValue()
                ,value.getValueId()});


    }

    @Transactional
    public void deleteValueById(int id) {

        final String DELETE_QUERY = "DELETE FROM value WHERE value_id = ?";

        jdbcTemplate.update(DELETE_QUERY,id);

    }
}
