package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Address;
import com.grad.project.nc.model.GoogleRegion;
import com.grad.project.nc.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by DeniG on 30.04.2017.
 */
@Repository
public class LocationDao extends AbstractDao<Location> {


    GoogleRegionDao googleRegionDao;

    LocationRowMapper mapper = new LocationRowMapper();

    @Autowired
    public LocationDao(JdbcTemplate jdbcTemplate, GoogleRegionDao googleRegionDao) {
        super(jdbcTemplate);
        this.googleRegionDao = googleRegionDao;
    }

    public Location getLocationByAddress(Address address) {
        return findOne(connection -> {
            final String FIND_QUERY =
                    "SELECT " +
                            "l.location_id, " +
                            "l.google_place_id, " +
                            "l.google_region_id " +
                            "FROM location l " +
                            "WHERE l.location_id = " +
                                "(SELECT ad.location_id " +
                                "FROM address ad " +
                                "WHERE ad.address_id = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
            preparedStatement.setLong(1, address.getAddressId());
            return preparedStatement;
        }, mapper);

    }

    @Override
    public Location add(Location entity) {
        KeyHolder keyHolder = executeInsert(connection -> {
            final String INSERT_QUERY = "INSERT INTO location (location.google_place_id, location.google_region_id) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
            preparedStatement.setString(1, entity.getGooglePlaceId());
            preparedStatement.setLong(2, entity.getGoogleRegion().getGoogleRegionId());
            return preparedStatement;
        });
        return find(getLongValue(keyHolder, "location_id"));
    }

    @Override
    @Transactional
    public Location update(Location entity) {
        executeUpdate(connection -> {
            final String UPDATE_QUERY =
                    "UPDATE location " +
                            "SET " +
                            "location.google_place_id = ?, " +
                            "location.google_region_id = ? " +
                            "WHERE location.location_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setString(1, entity.getGooglePlaceId());
            preparedStatement.setLong(2, entity.getGoogleRegion().getGoogleRegionId());
            preparedStatement.setLong(3, entity.getLocationId());
            return preparedStatement;
        });
        return entity;
    }

    @Override
    public Location find(Long id) {
        return findOne(connection -> {
            final String FIND_QUERY = "SELECT " +
                    "l.location_id, " +
                    "l.google_place_id, " +
                    "FROM location l " +
                    "WHERE l.location_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
            preparedStatement.setLong(1, id);
            return preparedStatement;

        }, mapper);
    }

    @Override
    public List<Location> findAll() {
        String findAllQuery = "SELECT \"location_id\", \"google_place_id\", \"google_region_id\" FROM \"location\"";

        return query(findAllQuery, new LocationRowMapper());
    }

    @Override
    public void delete(Location entity) {
        executeUpdate(connection -> {
            final String DELETE_QUERY =
                    "DELETE " +
                            "FROM location " +
                            "WHERE location.location_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setLong(1, entity.getLocationId());
            return preparedStatement;
        });

    }


    private class LocationProxy extends Location {
        @Override
        public GoogleRegion getGoogleRegion() {
            if (super.getGoogleRegion() == null) {
                super.setGoogleRegion(googleRegionDao.getGoogleRegionByLocation(this));
            }
            return super.getGoogleRegion();
        }
    }

    private final class LocationRowMapper implements RowMapper<Location> {

        @Override
        public Location mapRow(ResultSet resultSet, int i) throws SQLException {
            Location location = new LocationProxy();
            location.setLocationId(resultSet.getLong("location_id"));
            location.setGooglePlaceId(resultSet.getString("google_place_id"));
            return location;

        }
    }
}
