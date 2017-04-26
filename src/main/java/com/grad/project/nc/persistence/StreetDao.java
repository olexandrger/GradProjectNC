package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Street;
import com.sun.javafx.collections.MappingChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DeniG on 26.04.2017.
 */
public class StreetDao implements CrudDao<Street> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Street> mapper = new StreetMapper();

    @Override
    @Transactional
    public Street add(Street entity) {
        SimpleJdbcInsert insertStreetQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("street").usingGeneratedKeyColumns("street_id");
        Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("street_name", entity.getStreetName());
        parameters.put("city_id", entity.getCityId());
        Number newId = insertStreetQuery.executeAndReturnKey(parameters);
        entity.setCityId(newId.longValue());
        return entity;
    }

    @Override
    @Transactional
    public Street update(Street entity) {
        final String UPDATE_QUERY = "UPDATE street " +
                "SET street_name = ?, city_id = ? " +
                "WHERE street_id = ?";
        jdbcTemplate.update(UPDATE_QUERY, new Object[]{
                entity.getStreetName(),
                entity.getCityId(),
                entity.getStreetId()
        });
        return entity;
    }

    @Override
    @Transactional
    public Street find(long id) {
        final String SELECT_QUERY = "SELECT street_id, street_name, city_id FROM street WHERE street_id = ?";

        return jdbcTemplate.queryForObject(SELECT_QUERY, new Object[]{id}, mapper);
    }


    @Override
    @Transactional
    public Collection<Street> findAll() {
        final String SELECT_ALL_QUERY = "SELECT street_id, street_name, city_id FROM street";
        return jdbcTemplate.query(SELECT_ALL_QUERY, mapper);
    }

    @Override
    @Transactional
    public void delete(Street entity) {
        final String DELEE_QUERY = "DELETE FROM street WHERE street_id = ?";

        jdbcTemplate.update(DELEE_QUERY, entity.getStreetId());

    }

    private static class StreetMapper implements RowMapper<Street> {

        @Override
        public Street mapRow(ResultSet resultSet, int i) throws SQLException {
            Street street = new Street();
            street.setStreetId(resultSet.getLong("street_id"));
            street.setStreetName(resultSet.getString("street_name"));
            street.setCityId(resultSet.getLong("city_id"));
            return street;
        }
    }
}
