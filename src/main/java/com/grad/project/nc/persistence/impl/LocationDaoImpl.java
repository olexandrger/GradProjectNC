package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.Location;
import com.grad.project.nc.model.proxy.LocationProxy;
import com.grad.project.nc.persistence.AbstractDao;
import com.grad.project.nc.persistence.LocationDao;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LocationDaoImpl extends AbstractDao implements LocationDao {

    private static final String PK_COLUMN_NAME = "location_id";

    private final ObjectFactory<LocationProxy> proxyFactory;

    @Autowired
    public LocationDaoImpl(JdbcTemplate jdbcTemplate, ObjectFactory<LocationProxy> proxyFactory) {
        super(jdbcTemplate);
        this.proxyFactory = proxyFactory;
    }

    @Override
    public Location findAddressLocationById(Long addressId) {
        String query = "SELECT l.\"location_id\", l.\"google_place_id\", l.\"region_id\" " +
                "FROM \"location\" l " +
                "JOIN \"address\" a " +
                "ON a.\"location_id\" = l.\"location_id\" AND a.\"address_id\"=?";

        return findOne(query, new LocationRowMapper(), addressId);
    }

    @Override
    public List<Location> findByRegionId(Long regionId) {
        String query = "SELECT \"location_id\", \"google_place_id\", \"region_id\" " +
                "FROM \"location\" WHERE \"region_id\"=?";

        return findMultiple(query, new LocationRowMapper(), regionId);
    }

    @Override
    public Location add(Location location) {
        String insertQuery = "INSERT INTO \"location\" (\"google_place_id\", \"region_id\") VALUES (?, ?)";

        Long locationId = executeInsertWithId(insertQuery, PK_COLUMN_NAME, location.getGooglePlaceId(),
                location.getRegion().getRegionId());
        location.setLocationId(locationId);

        return location;
    }

    @Override
    public Location update(Location location) {
        String updateQuery = "UPDATE \"location\" SET \"google_place_id\"=?, \"region_id\"=? " +
                "WHERE \"location_id\"=?";

        executeUpdate(updateQuery, location.getGooglePlaceId(), location.getRegion().getRegionId(),
                location.getLocationId());

        return location;
    }

    @Override
    public Location find(Long id) {
        String findOneQuery = "SELECT \"location_id\", \"google_place_id\", \"region_id\" " +
                "FROM \"location\" WHERE \"location_id\"=?";

        return findOne(findOneQuery, new LocationRowMapper(), id);
    }

    @Override
    public List<Location> findAll() {
        String findAllQuery = "SELECT \"location_id\", \"google_place_id\", \"region_id\" FROM \"location\"";

        return findMultiple(findAllQuery, new LocationRowMapper());
    }

    @Override
    public void delete(Long id) {
        String deleteQuery = "DELETE FROM \"location\" " +
                "WHERE \"location_id\"=?";

        executeUpdate(deleteQuery, id);
    }

    private class LocationRowMapper implements RowMapper<Location> {

        @Override
        public Location mapRow(ResultSet resultSet, int i) throws SQLException {
            LocationProxy location = proxyFactory.getObject();

            location.setLocationId(resultSet.getLong("location_id"));
            location.setGooglePlaceId(resultSet.getString("google_place_id"));
            location.setRegionId(resultSet.getLong("region_id"));

            return location;
        }
    }
}