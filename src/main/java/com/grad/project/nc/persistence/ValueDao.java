package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Repository
public class ValueDao implements CrudDao<Value> {


    private JdbcTemplate jdbcTemplate;
    @Autowired
    public ValueDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Transactional
    @Override
    public Value add(Value value) {

        SimpleJdbcInsert insertProductQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("value")
                .usingGeneratedKeyColumns("value_id");

        Map<String, Object> parameters = new HashMap<String, Object>(4);
        parameters.put("product_characteristic_id", value.getProductCharacteristicId());
        parameters.put("number_value", value.getNumberValue());


        parameters.put("date_value", Timestamp.valueOf(value.getDateValue()));
        parameters.put("string_value", value.getStringValue());

        Number newId = insertProductQuery.executeAndReturnKey(parameters);
        value.setValueId(newId.longValue());

        return value;

    }

    @Transactional
    @Override
    public Value find(long id)  {
        final String SELECT_QUERY = "SELECT value_id" +
                ",product_characteristic_id" +
                ",number_value" +
                ",date_value" +
                ",string_value " +
                "FROM value " +
                "WHERE value_id = ?";

        Value value = null;
        try {
            value = jdbcTemplate.queryForObject(SELECT_QUERY,
                    new Object[]{id}, new ValueRowMapper());
        }catch (EmptyResultDataAccessException ex){

        }

        return value;
    }

    @Transactional
    @Override
    public Value update(Value value){
        final String UPDATE_QUERY = "UPDATE value SET product_characteristic_id = ?" +
                ", number_value = ?" +
                ", date_value = ?" +
                ", string_value = ? " +
                "WHERE value_id = ? ";

        jdbcTemplate.update(UPDATE_QUERY, value.getProductCharacteristicId()
                ,value.getNumberValue()
                ,Timestamp.valueOf(value.getDateValue())
                ,value.getStringValue()
                ,value.getValueId());

        return value;


    }

    @Transactional
    @Override
    public void delete(Value entity)  {

        final String DELETE_QUERY = "DELETE FROM value WHERE value_id = ?";
        jdbcTemplate.update(DELETE_QUERY,entity.getValueId());

    }


    @Override
    public Collection<Value> findAll() {
        final String SELECT_QUERY = "SELECT value_id" +
                ",product_characteristic_id" +
                ",number_value" +
                ",date_value" +
                ",string_value " +
                "FROM value ";

        return jdbcTemplate.query(SELECT_QUERY,new ValueRowMapper()) ;
    }

    private static final class ValueRowMapper implements RowMapper<Value> {
        @Override
        public Value mapRow(ResultSet rs, int rowNum) throws SQLException {
            Value value = new Value();

            value.setValueId(rs.getLong("value_id"));
            value.setProductCharacteristicId(rs.getLong("product_characteristic_id"));
            value.setNumberValue(rs.getLong("number_value"));
            value.setDateValue(rs.getTimestamp("date_value").toLocalDateTime());
            value.setStringValue(rs.getString("string_value"));

            return value;
        }
    }

}
