package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.Region;
import com.grad.project.nc.model.proxy.RegionProxy;
import com.grad.project.nc.persistence.AbstractDao;
import com.grad.project.nc.persistence.RegionDao;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RegionDaoImpl extends AbstractDao implements RegionDao {

    private static final String PK_COLUMN_NAME = "region_id";

    private final ObjectFactory<RegionProxy> proxyFactory;

    @Autowired
    public RegionDaoImpl(JdbcTemplate jdbcTemplate, ObjectFactory<RegionProxy> proxyFactory) {
        super(jdbcTemplate);
        this.proxyFactory = proxyFactory;
    }

    @Override
    public Region add(Region region) {
        String insertQuery = "INSERT INTO \"region\" (\"region_name\") VALUES (?)";

        Long regionId = executeInsertWithId(insertQuery, PK_COLUMN_NAME, region.getRegionName());

        region.setRegionId(regionId);

        return region;
    }

    @Override
    public Region update(Region region) {
        String updateQuery = "UPDATE \"region\" SET \"region_name\"=? WHERE \"region_id\"=?";

        executeUpdate(updateQuery, region.getRegionName(), region.getRegionId());

        return region;
    }

    @Override
    public Region find(Long id) {
        String findOneQuery = "SELECT \"region_id\", \"region_name\" FROM \"region\" " +
                "WHERE \"region_id\" = ?";

        return findOne(findOneQuery, new RegionRowMapper(), id);
    }

    @Override
    public List<Region> findAll() {
        String findAllQuery = "SELECT \"region_id\", \"region_name\" FROM \"region\"";

        return findMultiple(findAllQuery, new RegionRowMapper());
    }

    @Override
    public void delete(Long id) {
        String deleteQuery = "DELETE FROM \"region\" WHERE \"region_id\" = ?";

        executeUpdate(deleteQuery, id);
    }

    @Override
    public Region findLocationRegionById(Long locationId) {
        String query = "SELECT r.\"region_id\", r.\"region_name\" " +
                "FROM \"region\" r " +
                "JOIN \"location\" l " +
                "ON l.\"region_id\" = r.\"region_id\" " +
                "WHERE l.\"location_id\" = ?";

        return findOne(query, new RegionRowMapper(), locationId);
    }

    @Override
    public Region findByProductRegionPriceId(Long productRegionPriceId) {
        String query = "SELECT r.\"region_id\", r.\"region_name\" " +
                "FROM \"region\" r " +
                "JOIN \"product_region_price\" prp " +
                "ON prp.\"region_id\" = r.\"region_id\" " +
                "WHERE prp.\"price_id\" = ?";

        return findOne(query, new RegionRowMapper(), productRegionPriceId);
    }

    @Override
    public Region findByName(String regionName) {
        String query = "SELECT \"region_id\", \"region_name\" "+
                "FROM \"region\" " +
                "WHERE \"region_name\" = ?";
        return findOne(query, new RegionRowMapper(), regionName);
    }

    private class RegionRowMapper implements RowMapper<Region> {

        @Override
        public Region mapRow(ResultSet rs, int rowNum) throws SQLException {
            RegionProxy region = proxyFactory.getObject();

            region.setRegionId(rs.getLong("region_id"));
            region.setRegionName(rs.getString("region_name"));

            return region;
        }
    }
}
