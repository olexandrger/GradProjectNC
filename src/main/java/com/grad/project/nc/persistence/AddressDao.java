package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Address;
import com.grad.project.nc.model.Domain;
import com.grad.project.nc.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DeniG on 25.04.2017.
 */
@Repository
public class AddressDao extends AbstractDao<Address> {


    private LocationDao locationDao;

    private RowMapper<Address> mapper = new AddressMapper();

    @Autowired
    public AddressDao(JdbcTemplate jdbcTemplate, LocationDao locationDao) {
        super(jdbcTemplate);
        this.locationDao = locationDao;
    }

    @Override
    @Transactional
    public Address add(Address address) {
        KeyHolder keyHolder = executeInsert(connection -> {
            String query = "INSERT INTO address(address.apartment_number, address.location_id) VALUE (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, address.getApartmentNumber());
            preparedStatement.setLong(2, address.getLocation().getLocationId());
            return preparedStatement;
        });

        return find(address.getAddressId());

    }

    @Override
    public Address find(long id) {
        return findOne(connection -> {
            final String SELECT_QUERY = "SELECT address_id, apartment_number" +
                    " FROM address WHERE address_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, id);
            return preparedStatement;
        }, mapper);

    }

    @Override
    public Collection<Address> findAll() {
        return findMultiple(connection -> {
            final String SELECT_ALL_QUERY = "SELECT address_id, apartment_number " +
                    " FROM address";
            return connection.prepareStatement(SELECT_ALL_QUERY);

        }, mapper);

    }

    @Override
    @Transactional
    public Address update(Address address) {
        executeUpdate(connection -> {
            final String UPDATE_QUERY = "UPDATE address " +
                    "SET apartment_number = ?, location_id = ? " +
                    "WHERE address_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setInt(1, address.getApartmentNumber());
            preparedStatement.setLong(2, address.getLocation().getLocationId());
            preparedStatement.setLong(3, address.getAddressId());
            return preparedStatement;
        });
        return address;
    }

    @Override
    @Transactional
    public void delete(Address adress) {
        executeUpdate(connection -> {
            final String DELETE_QUERY = "DELETE FROM address WHERE address_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setLong(1, adress.getAddressId());
            return preparedStatement;
        });
    }

    public Address getAddressByDomain(Domain domain) {
        return findOne(connection -> {
            final String QUERY = "SELECT " +
                    "address_id, " +
                    "apartment_number " +
                    "FROM address " +
                    "WHERE address_id = " +
                    "(SELECT d.address_id " +
                    "FROM \"domain\" d " +
                    "WHERE d.domain_id = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
            preparedStatement.setLong(1, domain.getDomainId());
            return preparedStatement;
        }, mapper);
    }

    private class AddressProxy extends Address {
        @Override
        public Location getLocation() {
            if (super.getLocation() == null) {
                super.setLocation(locationDao.getLocationByAddress(this));
            }
            return super.getLocation();
        }
    }

    private class AddressMapper implements RowMapper<Address> {

        @Override
        public Address mapRow(ResultSet resultSet, int i) throws SQLException {
            Address address = new AddressProxy();
            address.setAddressId(resultSet.getLong("address_id"));
            address.setApartmentNumber(resultSet.getInt("apartment_number"));
            return address;
        }
    }


}
