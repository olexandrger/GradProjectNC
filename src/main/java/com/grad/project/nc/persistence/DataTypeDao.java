package com.grad.project.nc.persistence;

import com.grad.project.nc.model.DataType;
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
import java.util.Map;


@Repository
public class DataTypeDao implements CrudDao<DataType> {


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DataTypeDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Transactional
    @Override
    public DataType add(DataType dataType) {

        SimpleJdbcInsert insertProductQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("data_type")
                .usingGeneratedKeyColumns("data_type_id");

        Map<String, Object> parameters = new HashMap<String, Object>(1);
        parameters.put("data_type", dataType.getDataType());

        Number newId = insertProductQuery.executeAndReturnKey(parameters);
        dataType.setDataTypeId(newId.longValue());

        return dataType;
    }



    @Transactional
    @Override
    public DataType update(DataType dataType) {
        final String UPDATE_QUERY = "UPDATE data_type SET data_type = ?" +
                "WHERE data_type_id = ? ";

        jdbcTemplate.update(UPDATE_QUERY, dataType.getDataType(),dataType.getDataTypeId());

        return dataType;
    }

    @Transactional
    @Override
    public DataType find(long id) {
        final String SELECT_QUERY = "SELECT data_type,data_type_id FROM data_type where data_type_id = ?";

        DataType dataType = null;
        try {
            dataType = jdbcTemplate.queryForObject(SELECT_QUERY,
                    new Object[]{id}, new DataTypeRowMapper());
        } catch (EmptyResultDataAccessException ex){

        }
        return dataType;
    }

    @Transactional
    @Override
    public Collection<DataType> findAll() {
        final String SELECT_QUERY = "SELECT data_type,data_type_id FROM data_type ";
        return jdbcTemplate.query(SELECT_QUERY,new DataTypeRowMapper());
    }

    @Transactional
    @Override
    public void delete(DataType entity) {
        final String DELETE_QUERY = "DELETE FROM data_type WHERE data_type_id = ?";

        jdbcTemplate.update(DELETE_QUERY,entity.getDataTypeId());

    }

    private static final class DataTypeRowMapper implements RowMapper<DataType> {
        @Override
        public DataType mapRow(ResultSet rs, int rowNum) throws SQLException {
            DataType dataType = new DataType();

            dataType.setDataTypeId(rs.getLong("data_type_id"));
            dataType.setDataType(rs.getString("data_type"));

            return dataType;
        }
    }



}
