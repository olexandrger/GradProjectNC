package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Region;
import com.grad.project.nc.persistence.mappers.RegionRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roman Savuliak on 25.04.2017.
 */
@Component
public class RegionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void insertRegion(Region region){

        SimpleJdbcInsert insertRegionQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("region")
                .usingGeneratedKeyColumns("region_id");

        Map<String, Object> parameters = new HashMap<String, Object>(1);
        parameters.put("region_name", region.getRegionName());

        Number newId = insertRegionQuery.executeAndReturnKey(parameters);
        region.setRegionId(newId.intValue());

    }

    @Transactional
    public Region readRegionById(int id){
        final String SELECT_QUERY = "SELECT region_id, region_name FROM region WHERE region_id = ?";

        Region region = jdbcTemplate.queryForObject(SELECT_QUERY, new Object[]{id}, new RegionRowMapper());
        return region;
    }

    @Transactional
    public void updateRegion(Region region){
        final String UPDATE_QUERY = "UPDATE region SET region_name = ?" + "WHERE region_id = ?";

        jdbcTemplate.update(UPDATE_QUERY, new Object[]{region.getRegionName(), region.getRegionId()});
    }

    @Transactional
    public void deleteRegionById(int id){
        final String DELETE_QUERY = "DELETE FROM region WHERE region_id = ?";
        jdbcTemplate.update(DELETE_QUERY, id);
    }


}
