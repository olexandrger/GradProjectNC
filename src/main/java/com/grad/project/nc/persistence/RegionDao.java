package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Region;
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
 * Created by Roman Savuliak on 25.04.2017.
 */
@Repository
public class RegionDao implements CrudDao<Region>{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public Region add(Region region) {

        SimpleJdbcInsert insertRegionQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("region")
                .usingGeneratedKeyColumns("region_id");

        Map<String, Object> parameters = new HashMap<String, Object>(1);
        parameters.put("region_name", region.getRegionName());

        Number newId = insertRegionQuery.executeAndReturnKey(parameters);
        region.setRegionId(newId.longValue());

        return region;
    }

    @Override
    @Transactional
    public Region update(Region region) {
        final String UPDATE_QUERY = "UPDATE region SET region_name = ?" + "WHERE region_id = ?";
        jdbcTemplate.update(UPDATE_QUERY, new Object[]{region.getRegionName(), region.getRegionId()});
        return region;
    }

    @Override
    @Transactional
    public Region find(long id) {
        final String SELECT_QUERY = "SELECT region_id, region_name FROM region WHERE region_id = ?";
        Region region = null;
        try{
            region = jdbcTemplate.queryForObject(SELECT_QUERY, new Object[]{id}, new RegionRowMapper());
        } catch (EmptyResultDataAccessException ex){

        }
        return region;
    }

    @Override
    @Transactional
    public Collection<Region> findAll() {
        final String SELECT_QUERY = "SELECT region_id, region_name FROM region";
        return jdbcTemplate.query(SELECT_QUERY, new RegionRowMapper());
    }

    @Override
    @Transactional
    public void delete(Region region) {
        final String DELETE_QUERY = "DELETE FROM region WHERE region_id = ?";
        jdbcTemplate.update(DELETE_QUERY, region.getRegionId());
    }

    private static final class RegionRowMapper implements RowMapper<Region> {
        @Override
        public Region mapRow(ResultSet rs, int rowNum) throws SQLException {
            Region region = new Region();

            region.setRegionId(rs.getLong("region_id"));
            region.setRegionName(rs.getString("region_name"));

            return region;
        }

    }
}
