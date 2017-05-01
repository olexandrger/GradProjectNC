package com.grad.project.nc.persistence;

import com.grad.project.nc.model.GoogleRegion;
import com.grad.project.nc.model.ProductRegionPrice;
import com.grad.project.nc.model.Region;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roman Savuliak on 25.04.2017.
 */
@Repository
public class RegionDao extends AbstractDao<Region> {

    RegionRowMapper regionRowMapper = new RegionRowMapper();

    @Autowired
    RegionDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    @Transactional
    public Region add(Region region) {
        KeyHolder keyHolder = executeInsert(connection -> {
            final String INSERT_QUERY =
                    "INSERT INTO region (region.region_name) " +
                            "VALUES ?";
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
            preparedStatement.setString(1, region.getRegionName());
            return preparedStatement;
        });
        return find(getLongValue(keyHolder, "region_id"));
    }

    @Override
    @Transactional
    public Region update(Region region) {

        executeUpdate(connection -> {
            final String UPDATE_QUERY =
                    "UPDATE region " +
                            "SET region_name = ? " +
                            "WHERE region_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setString(1, region.getRegionName());
            preparedStatement.setLong(2, region.getRegionId());
            return preparedStatement;
        });

        return region;
    }

    @Override
    public Region find(long id) {
        return findOne(connection -> {
            final String SELECT_QUERY =
                    "SELECT " +
                            "region_id, " +
                            "region_name " +
                            "FROM region " +
                            "WHERE region_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, id);
            return preparedStatement;
        }, regionRowMapper);
    }

    @Override
    public Collection<Region> findAll() {
        return findMultiple(connection -> {
            final String SELECT_QUERY = "SELECT region_id, region_name FROM region";
            return connection.prepareStatement(SELECT_QUERY);
        }, regionRowMapper);
    }

    @Override
    @Transactional
    public void delete(Region region) {
        executeUpdate(connection -> {
            final String DELETE_QUERY = "DELETE FROM region WHERE region_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setLong(1, region.getRegionId());
            return preparedStatement;
        });
    }

    public Region findByGoogleRegion(GoogleRegion googleRegion) {
        return findOne(connection -> {
            final String SELECT_QUERY =
                    "SELECT " +
                            "r.region_id, " +
                            "r.region_name " +
                            "FROM region r " +
                            "WHERE r.region_id = " +
                            "(SELECT " +
                            "gr.region_id " +
                            "FROM google_region gr " +
                            "WHERE gr.google_region_id = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, googleRegion.getGoogleRegionId());
            return preparedStatement;
        }, regionRowMapper);
    }

    public Region fingRegionByProductRegionPrice(ProductRegionPrice productRegionPrice){
        return findOne(connection -> {
            final String SELECT_QUERY =
                    "SELECT " +
                            "r.region_id, " +
                            "r.region_name " +
                            "FROM region r " +
                            "WHERE r.region_id = " +
                            "(SELECT " +
                            "prp.region_id " +
                            "FROM product_region_price prp " +
                            "WHERE prp.price_id = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, productRegionPrice.getPriceId());
            return preparedStatement;
        }, regionRowMapper);

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
