package com.grad.project.nc.persistence;

import com.grad.project.nc.model.City;
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
public class CityDao implements CrudDao<City> {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<City> mapper = new CityMapper();

    @Override
    @Transactional
    public City add(City entity) {
        SimpleJdbcInsert insertCityQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("city")
                .usingGeneratedKeyColumns("city_id");
        Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("city_name", entity.getCityName());
        parameters.put("region_id", entity.getRegionId());
        Number newId = insertCityQuery.executeAndReturnKey(parameters);
        entity.setCityId(newId.longValue());
        return entity;
    }

    @Override
    @Transactional
    public City update(City entity) {
        final String UPDATE_QUERY = "UPDATE ciy SET city_name = ?, region_id = ? WHERE city_id = ?";
        jdbcTemplate.update(UPDATE_QUERY, new Object[]{
                entity.getCityName(),
                entity.getRegionId(),
                entity.getCityId(),
        });

        return entity;
    }

    @Override
    @Transactional
    public City find(long id) {
        final String SELECT_QURY = "SELECT city_id, city_name, region_id FROM city WHERE ciy_id = ?";
        return jdbcTemplate.queryForObject(SELECT_QURY, new Object[]{id}, mapper);
    }

    @Override
    @Transactional
    public Collection<City> findAll() {
        final String SELECT_ALL_QURY = "SELECT city_id, city_name, region_id FROM city";
        return jdbcTemplate.query(SELECT_ALL_QURY, mapper);
    }

    @Override
    @Transactional
    public void delete(City entity) {
        final String DELEE_QUERY = "DELETE FROM city WHERE city_id = ?";

        jdbcTemplate.update(DELEE_QUERY, entity.getCityId());

    }

    private static class CityMapper implements RowMapper<City>{

        @Override
        public City mapRow(ResultSet resultSet, int i) throws SQLException {
            City city = new City();
            city.setCityId(resultSet.getLong("city_id"));
            city.setCityName(resultSet.getString("city_name"));
            city.setRegionId(resultSet.getLong("region_id"));
            return city;
        }
    }
}
