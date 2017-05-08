package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.Address;
import com.grad.project.nc.model.proxy.AddressProxy;
import com.grad.project.nc.persistence.AbstractDao;
import com.grad.project.nc.persistence.AddressDao;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AddressDaoImpl extends AbstractDao implements AddressDao {

    private static final String PK_COLUMN_NAME = "address_id";

    private final ObjectFactory<AddressProxy> proxyFactory;

    @Autowired
    public AddressDaoImpl(JdbcTemplate jdbcTemplate, ObjectFactory<AddressProxy> proxyFactory) {
        super(jdbcTemplate);
        this.proxyFactory = proxyFactory;
    }

    @Override
    public Address add(Address address) {
        String insertQuery = "INSERT INTO \"address\" (\"apartment_number\", \"location_id\") VALUES (?, ?)";

        Long addressId = executeInsertWithId(insertQuery, PK_COLUMN_NAME, address.getApartmentNumber(),
                address.getLocation().getLocationId());
        address.setAddressId(addressId);

        return address;
    }

    @Override
    public Address find(Long id) {
        String findOneQuery = "SELECT \"address_id\", \"apartment_number\", \"location_id\" " +
                "FROM \"address\" WHERE address_id=?";

        return findOne(findOneQuery, new AddressRowMapper(), id);
    }

    @Override
    public List<Address> findAll() {
        String findAllQuery = "SELECT \"address_id\", \"apartment_number\", \"location_id\" " +
                "FROM \"address\"";

        return findMultiple(findAllQuery, new AddressRowMapper());
    }

    @Override
    public Address update(Address address) {
        String updateQuery = "UPDATE \"address\" SET \"apartment_number\"=?, \"location_id\"=? " +
                "WHERE \"address_id\"=?";

        executeUpdate(updateQuery, address.getApartmentNumber(), address.getLocation().getLocationId(),
                address.getAddressId());
        return address;
    }

    @Override
    public void delete(Long id) {
        String deleteQuery = "DELETE FROM \"address\" WHERE \"address_id\"=?";

        executeUpdate(deleteQuery, id);
    }

    @Override
    public Address findDomainAddressById(Long domainId) {
        String query = "SELECT a.\"address_id\", a.\"apartment_number\", a.\"location_id\" " +
                "FROM \"address\" a " +
                "JOIN \"domain\" d " +
                "ON a.\"address_id\"=d.\"address_id\" AND d.\"domain_id\"=?";

        return findOne(query, new AddressRowMapper(), domainId);
    }

    private class AddressRowMapper implements RowMapper<Address> {

        @Override
        public Address mapRow(ResultSet resultSet, int i) throws SQLException {
            AddressProxy address = proxyFactory.getObject();

            address.setAddressId(resultSet.getLong("address_id"));
            address.setApartmentNumber(resultSet.getString("apartment_number"));
            address.setLocationId(resultSet.getLong("location_id"));

            return address;
        }
    }
}