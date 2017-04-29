package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Building;
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

/**
 * Created by DeniG on 25.04.2017.
 */
@Repository
public class BuildingDao implements CrudDao<Building> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Building> mapper = new BuildingMapper();

    @Override
    @Transactional
    public Building add(Building building) {

        SimpleJdbcInsert insertBuildingQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("building")
                .usingGeneratedKeyColumns("building_id");
        Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("building_number", building.getBuildingNumber());
        parameters.put("street_id", building.getStreetId());
        Number newId = insertBuildingQuery.executeAndReturnKey(parameters);
        building.setBuildingId(newId.longValue());
        return building;
    }

    @Override
    @Transactional
    public Building update(Building entity) {
        final String UPDARE_QUERY = "UPDATE building " +
                " SET street_id = ?, building_number = ? " +
                "WHERE building_id = ?";
        jdbcTemplate.update(UPDARE_QUERY, new Object[]{
                entity.getStreetId(),
                entity.getBuildingNumber(),
                entity.getBuildingId()
        });
        return entity;
    }

    @Override
    @Transactional
    public Building find(long id) {
        final String SELECT_QUERY = "SELECT street_id, building_number, building_id " +
                "FROM building " +
                "WHERE building_id = ?";

        Building building = null;
        try {
            building = jdbcTemplate.queryForObject(SELECT_QUERY, new Object[]{id}, mapper);
        } catch (EmptyResultDataAccessException ex){

        }
        return building;

    }

    @Override
    @Transactional
    public Collection<Building> findAll() {
        final String SELECT_ALL_QUERY = "SELECT street_id, building_number, building_id " +
                "FROM building ";
        return jdbcTemplate.query(SELECT_ALL_QUERY, mapper);

    }

    @Override
    @Transactional
    public void delete(Building entity) {
        final String DELETE_QUERY = "DELETE FROM building WHERE building_id = ?";
        jdbcTemplate.update(DELETE_QUERY, entity.getBuildingId());

    }

    private static class BuildingMapper implements RowMapper<Building> {
        @Override
        public Building mapRow(ResultSet resultSet, int i) throws SQLException {
            Building building = new Building();
            building.setBuildingId(resultSet.getLong("building_id"));
            building.setBuildingNumber(resultSet.getLong("building_number"));
            building.setStreetId(resultSet.getLong("street_id"));
            return building;
        }
    }
}
