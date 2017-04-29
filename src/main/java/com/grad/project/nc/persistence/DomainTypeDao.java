package com.grad.project.nc.persistence;

import com.grad.project.nc.model.DomainType;
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

/**
 * Created by Alex on 4/26/2017.
 */
@Repository
public class DomainTypeDao implements CrudDao<DomainType>  {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DomainTypeDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public DomainType add(DomainType entity) {
        final String INSERT_QUERY = "INSERT INTO domain_type(domain_type_name) VALUES (?);";


        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(INSERT_QUERY, new String[]{"domain_type_id"});
                        ps.setString(1, entity.getDomainTypeName());
                        return ps;
                    }
                },
                keyHolder);

        entity.setDomainTypeId(keyHolder.getKey().longValue());

        return entity;
    }

    @Override
    public DomainType update(DomainType entity) {
        final String UPDATE_QUERY = "UPDATE domain_type SET domain_type_name = ? WHERE domain_type_id = ? ";

        jdbcTemplate.update(UPDATE_QUERY, entity.getDomainTypeName(),entity.getDomainTypeId());

        return entity;

    }

    @Override
    public DomainType find(long id) {
        final String SELECT_QUERY = "SELECT domain_type_name FROM domain_type WHERE domain_type_id = ?";


        DomainType domainType = null;
        try {
            domainType = jdbcTemplate.queryForObject(SELECT_QUERY,
                    new Object[]{id}, new DomainTypeRowMapper());
        } catch (EmptyResultDataAccessException ex){

        }

        return domainType;
    }

    @Override
    public Collection<DomainType> findAll() {
        final String SELECT_QUERY = "SELECT domain_type_id, domain_type_name  FROM domain_type" ;
        return jdbcTemplate.query(SELECT_QUERY, new DomainTypeRowMapper());

    }

    @Override
    public void delete(DomainType entity) {
        final String DELETE_QUERY = "DELETE FROM domain_type WHERE domain_type_id = ?";
        jdbcTemplate.update(DELETE_QUERY,entity.getDomainTypeId());

    }

    private static final class DomainTypeRowMapper implements RowMapper<DomainType>{

        @Override
        public DomainType mapRow(ResultSet rs, int rowNum) throws SQLException {
            DomainType domainType = new DomainType();

            domainType.setDomainTypeId(rs.getLong("domain_type_id"));
            domainType.setDomainTypeName(rs.getString("domain_type_name"));
            return domainType;
        }
    }
}
