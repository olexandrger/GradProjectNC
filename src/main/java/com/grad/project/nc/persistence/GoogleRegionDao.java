package com.grad.project.nc.persistence;

import com.grad.project.nc.model.GoogleRegion;
import com.grad.project.nc.model.Location;
import com.grad.project.nc.model.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by DeniG on 30.04.2017.
 */
@Repository
public class GoogleRegionDao extends AbstractDao<GoogleRegion> {

    GoogleRegionRowMapper mapper = new GoogleRegionRowMapper();

    RegionDao regionDao;

    @Autowired
    public GoogleRegionDao(JdbcTemplate jdbcTemplate, RegionDao regionDao) {
        super(jdbcTemplate);
        this.regionDao = regionDao;
    }

    public GoogleRegion getGoogleRegionByLocation(Location location) {
        return findOne(connection -> {
            final String SELECT_QUERY =
                    "SELECT " +
                            "gr.google_region_id, " +
                            "gr.google_region_name " +
                            "FROM google_region gr " +
                            "WHERE gr.google_region_id = " +
                            "(SELECT " +
                            "lc.google_region_id " +
                            "FROM location lc " +
                            "WHERE lc.location_id = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, location.getLocationId());
            return preparedStatement;
        }, mapper);

    }

    @Override
    public GoogleRegion add(GoogleRegion entity) {
        KeyHolder keyHolder = executeInsert(connection -> {
            final String INSERT_QUERY = "INSERT INTO google_region (" +
                    "google_region.google_region_name, " +
                    "google_region.region_id) " +
                    "VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
            preparedStatement.setString(1, entity.getGoogleRegionName());
            preparedStatement.setLong(2, entity.getRegion().getRegionId());
            return preparedStatement;
        });
        return find(getLongValue(keyHolder, "google_region_id"));

    }

    @Override
    @Transactional
    public GoogleRegion update(GoogleRegion entity) {
        executeUpdate(connection -> {
            final String UPDATE_QUERY =
                    "UPDATE google_region " +
                            "SET (" +
                            "google_region.google_region_name = ?, " +
                            "google_region.region_id = ?) " +
                            "WHERE google_region_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setString(1, entity.getGoogleRegionName());
            preparedStatement.setLong(2, entity.getRegion().getRegionId());
            preparedStatement.setLong(3, entity.getGoogleRegionId());
            return preparedStatement;
        });
        return (entity instanceof GoogleRegionProxy) ? (entity) : (find(entity.getGoogleRegionId()));
    }

    @Override
    public GoogleRegion find(long id) {
        return findOne(connection -> {
            final String SELECT_QUERY =
                    "SELECT " +
                            "gr.google_region_id, " +
                            "gr.google_region_name " +
                            "FROM google_region gr " +
                            "WHERE gr.google_region_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, id);
            return preparedStatement;
        }, mapper);
    }

    @Override
    public Collection<GoogleRegion> findAll() {
        return findMultiple(connection -> {
            final String SELECT_QUERY =
                    "SELECT " +
                            "gr.google_region_id, " +
                            "gr.google_region_name " +
                            "FROM google_region gr ";
            return connection.prepareStatement(SELECT_QUERY);
        }, mapper);
    }

    @Override
    public void delete(GoogleRegion entity) {
        executeUpdate(connection -> {
            final String DELETE_QUERY = "" +
                    "DELETE FROM google_region " +
                    "WHERE google_region_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setLong(1, entity.getGoogleRegionId());
            return preparedStatement;
        });

    }

    private class GoogleRegionProxy extends GoogleRegion {
        @Override
        public Region getRegion() {
            if (super.getRegion() == null) {
                super.setRegion(regionDao.findByGoogleRegion(this));
            }
            return super.getRegion();
        }
    }

    private class GoogleRegionRowMapper implements RowMapper<GoogleRegion> {

        @Override
        public GoogleRegion mapRow(ResultSet resultSet, int i) throws SQLException {
            GoogleRegion googleRegion = new GoogleRegionProxy();
            googleRegion.setGoogleRegionId(resultSet.getLong("google_region_id"));
            googleRegion.setGoogleRegionName(resultSet.getString("google_region_name"));
            return googleRegion;
        }
    }
}
