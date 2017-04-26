package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DeniG on 25.04.2017.
 */
@Repository
public class AddressDao implements CrudDao<Address> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Address> mapper = new AddressMapper();

    @Override
    @Transactional
    public Address add(Address address) {
        SimpleJdbcInsert insertAddressQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("address").usingGeneratedKeyColumns("address_id");
        Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("apartment_number", address.getApartmentNumber());
        parameters.put("building_id", address.getBuildingId());
        Number newId = insertAddressQuery.executeAndReturnKey(parameters);
        address.setAddressId(newId.longValue());
        return address;
    }

    @Override
    @Transactional
    public Address find(long id) {
        final String SELECT_QUERY = "SELECT address_id, apartment_number, building_id" +
                " FROM address WHERE address_id = ?";

        Address address = null;
        try {
            address = jdbcTemplate.queryForObject(SELECT_QUERY,
                    new Object[]{id}, mapper);
        } catch (EmptyResultDataAccessException ex){

        }
        return address;

    }

    @Override
    @Transactional
    public Collection<Address> findAll() {
        final String SELECT_ALL_QUERY = "SELECT address_id, apartment_number, building_id" +
                " FROM address";
        return jdbcTemplate.query(SELECT_ALL_QUERY, mapper);

    }

    @Override
    @Transactional
    public Address update(Address address) {
        final String UPDATE_QUERY = "UPDATE address " +
                "SET apartment_number = ?, building_id = ? " +
                "WHERE address_id = ?";

        jdbcTemplate.update(UPDATE_QUERY, new Object[]{
                address.getApartmentNumber(),
                address.getBuildingId(),
                address.getAddressId()});
        return address;
    }

    @Override
    @Transactional
    public void delete(Address adress) {
        final String DELETE_QUERY = "DELETE FROM address WHERE address_id = ?";

        jdbcTemplate.update(DELETE_QUERY, adress.getAddressId());
    }

    private static class AddressMapper implements RowMapper<Address> {

        @Override
        public Address mapRow(ResultSet resultSet, int i) throws SQLException {
            Address address = new Address();
            address.setAddressId(resultSet.getLong("address_id"));
            address.setApartmentNumber(resultSet.getInt("apartment_number"));
            address.setBuildingId(resultSet.getLong("building_id"));
            return address;
        }
    }



}
