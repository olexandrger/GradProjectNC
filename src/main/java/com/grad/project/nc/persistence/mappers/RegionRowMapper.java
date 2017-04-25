package com.grad.project.nc.persistence.mappers;

import com.grad.project.nc.model.Region;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Roman Savuliak on 25.04.2017.
 */
public class RegionRowMapper implements RowMapper<Region> {
    @Override
    public Region mapRow(ResultSet rs, int rowNum) throws SQLException {
        Region region = new Region();

        region.setRegionId(rs.getInt("region_id"));
        region.setRegionName(rs.getString("region_name"));

        return region;
    }
}
