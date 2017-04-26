package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class DomainDao implements CrudDao<Domain> {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DomainDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Domain add(Domain entity) {

        final String INSERT_QUERY = "INSERT INTO domain(" +
                "domain_name, address_id, domain_type_id)" +
                " VALUES (?, ?, ?);";


        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(INSERT_QUERY, new String[]{"domain_id"});
                        ps.setString(1, entity.getDomainName());
                        ps.setLong(2, entity.getAddressId());
                        ps.setLong(3,entity.getDomainTypeId());
                        return ps;
                    }
                },
                keyHolder);

        entity.setDomainId(keyHolder.getKey().longValue());

        return entity;
    }

    @Override
    public Domain update(Domain entity) {
        final String UPDATE_QUERY = "UPDATE domain SET domain_name = ?, address_id = ? , domain_type_id = ? WHERE domain_id = ? ";

        jdbcTemplate.update(UPDATE_QUERY, entity.getDomainName(),entity.getAddressId(),entity.getDomainTypeId(),entity.getDomainId());

        return entity;
    }

    @Override
    public Domain find(long id) {
        final String SELECT_QUERY = "SELECT domain_name" +
                ",address_id" +
                ",domain_type_id" +
                "FROM domain " +
                "WHERE domain_id = ?";

        Domain domain = null;
        try {
            domain = jdbcTemplate.queryForObject(SELECT_QUERY,
                    new Object[]{id}, new DomainRowMapper());
        } catch (EmptyResultDataAccessException ex){

        }


        return domain;
    }

    @Override
    public Collection<Domain> findAll() {
        final String SELECT_QUERY = "SELECT domain_id,domain_name" +
                ",address_id" +
                ",domain_type_id" +
                "FROM domain ";
        return jdbcTemplate.query(SELECT_QUERY, new DomainRowMapper());

    }

    @Override
    public void delete(Domain entity) {
        final String DELETE_QUERY = "DELETE FROM domain WHERE domain_id = ?";
        jdbcTemplate.update(DELETE_QUERY,entity.getDomainId());

    }

    private static final class DomainRowMapper implements RowMapper<Domain>{

        @Override
        public Domain mapRow(ResultSet rs, int rowNum) throws SQLException {
            Domain domain = new Domain();

            domain.setDomainId(rs.getLong("domain_id"));
            domain.setDomainName(rs.getString("domain_name"));
            domain.setAddressId(rs.getLong("address_id"));
            domain.setDomainTypeId(rs.getLong("domain_type_id"));
            return domain;
        }
    }


}
