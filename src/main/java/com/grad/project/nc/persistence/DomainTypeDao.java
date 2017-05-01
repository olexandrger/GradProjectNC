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
import com.grad.project.nc.model.Domain;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by Alex on 4/26/2017.
 */
@Repository
public class DomainTypeDao extends AbstractDao<DomainType> {


    @Autowired
    public DomainTypeDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public DomainType add(DomainType entity) {

        KeyHolder keyHolder = executeInsert(connection -> {
            final String INSERT_QUERY = "INSERT INTO domain_type(domain_type_name) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
            preparedStatement.setString(1, entity.getDomainTypeName());
            return preparedStatement;
        });
        entity.setDomainTypeId(getLongValue(keyHolder, "domain_type_id"));

        return entity;
    }

    @Override
    @Transactional
    public DomainType update(DomainType entity) {
        executeUpdate(connection -> {
            final String UPDATE_QUERY = "UPDATE domain_type SET domain_type_name = ? WHERE domain_type_id = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setString(1, entity.getDomainTypeName());
            preparedStatement.setLong(2, entity.getDomainTypeId());
            return preparedStatement;
        });

        return entity;

    }

    @Override
    public DomainType find(long id) {

        return findOne(connection -> {
            final String SELECT_QUERY =
                    "SELECT " +
                            "domain_type_name, " +
                            "domain_type_id " +
                            "FROM " +
                            "domain_type " +
                            "WHERE " +
                            "domain_type_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, id);
            return preparedStatement;
        }, new DomainTypeRowMapper());

    }

    @Override
    public Collection<DomainType> findAll() {
        return findMultiple(connection -> {
            final String SELECT_QUERY =
                    "SELECT " +
                            "domain_type_id, " +
                            "domain_type_name  " +
                            "FROM domain_type";
            return connection.prepareStatement(SELECT_QUERY);
        }, new DomainTypeRowMapper());

    }

    @Override
    public void delete(DomainType entity) {
        executeUpdate(connection -> {
            final String DELETE_QUERY = "DELETE FROM domain_type WHERE domain_type_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setLong(1, entity.getDomainTypeId());
            return preparedStatement;
        });

    }

    public DomainType getDomainTypeByDomain(Domain domain) {
        return findOne(connection -> {
            //language=GenericSQL
            final String SELECT_QUERY = "SELECT " +
                    "domain_type_name, " +
                    "domain_type_id  " +
                    "FROM domain_type " +
                    "WHERE domain_type_id = (" +
                    "SELECT d.domain_type_id " +
                    "FROM domain d " +
                    "WHERE d.domain_id = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, domain.getDomainId());
            return preparedStatement;
        }, new DomainTypeRowMapper());

    }

    private final class DomainTypeRowMapper implements RowMapper<DomainType> {

        @Override
        public DomainType mapRow(ResultSet rs, int rowNum) throws SQLException {
            DomainType domainType = new DomainType();

            domainType.setDomainTypeId(rs.getLong("domain_type_id"));
            domainType.setDomainTypeName(rs.getString("domain_type_name"));
            return domainType;
        }
    }
}
