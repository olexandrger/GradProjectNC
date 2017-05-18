package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.Domain;
import com.grad.project.nc.model.User;
import com.grad.project.nc.model.proxy.DomainProxy;
import com.grad.project.nc.persistence.AbstractDao;
import com.grad.project.nc.persistence.DomainDao;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DomainDaoImpl extends AbstractDao implements DomainDao {

    private static final String PK_COLUMN_NAME = "domain_id";

    private final ObjectFactory<DomainProxy> proxyFactory;

    @Autowired
    public DomainDaoImpl(JdbcTemplate jdbcTemplate, ObjectFactory<DomainProxy> proxyFactory) {
        super(jdbcTemplate);
        this.proxyFactory = proxyFactory;
    }

    @Override
    public Domain add(Domain domain) {
        String insertQuery = "INSERT INTO \"domain\" (\"domain_name\", \"address_id\", \"domain_type_id\") " +
                "VALUES (?, ?, ?)";

        Long domainId = executeInsertWithId(insertQuery, PK_COLUMN_NAME, domain.getDomainName(),
                domain.getAddress().getAddressId(), domain.getDomainType().getCategoryId());

        domain.setDomainId(domainId);

        return domain;
    }

    @Override
    public Domain update(Domain domain) {
        String updateQuery = "UPDATE \"domain\" SET \"domain_name\" = ?, \"address_id\" = ?, " +
                "\"domain_type_id\" = ? WHERE \"domain_id\" = ?";

        executeUpdate(updateQuery, domain.getDomainName(), domain.getAddress().getAddressId(),
                domain.getDomainType().getCategoryId(), domain.getDomainId());
        return domain;
    }

    @Override
    public Domain find(Long id) {
        String findOneQuery = "SELECT \"domain_id\", \"domain_name\", \"address_id\", " +
                "\"domain_type_id\" FROM \"domain\" WHERE \"domain_id\" = ?";

        return findOne(findOneQuery, new DomainRowMapper(), id);
    }

    @Override
    public List<Domain> findAll() {
        String findAllQuery = "SELECT \"domain_id\", \"domain_name\", \"address_id\", \"domain_type_id\" " +
                "FROM \"domain\"";
        return findMultiple(findAllQuery, new DomainRowMapper());
    }

    @Override
    public void delete(Long id) {
        String deleteQuery = "DELETE FROM \"domain\" WHERE \"domain_id\" = ?";
        executeUpdate(deleteQuery, id);
    }

    @Override
    public List<Domain> findByUserId(Long userId) {
        String query = "SELECT d.\"domain_id\", d.\"domain_name\", d.\"address_id\", d.\"domain_type_id\" " +
                "FROM \"domain\" d " +
                "INNER JOIN \"user_domain\" ud " +
                "ON d.\"domain_id\" = ud.\"domain_id\" " +
                "WHERE ud.\"user_id\" = ?";

        return findMultiple(query, new DomainRowMapper(), userId);
    }

    @Override
    public void deleteDomainUser(Long domainId, Long userId) {
        String deleteQuery = "DELETE FROM \"user_domain\" WHERE \"domain_id\" = ? AND " +
                "\"user_id\" = ?";

        executeUpdate(deleteQuery, domainId, userId);
    }

    @Override
    public void deleteDomainUsers(Long domainId) {
        String deleteQuery = "DELETE FROM \"user_domain\" WHERE \"domain_id\" = ? ";
        executeUpdate(deleteQuery, domainId);
    }

    @Override
    public void addDomainUser(Long domainId, Long userId) {
        String insertQuery = "INSERT INTO \"user_domain\" (\"domain_id\", \"user_id\") VALUES (?, ?)";

        executeUpdate(insertQuery, domainId, userId);
    }

    private class DomainRowMapper implements RowMapper<Domain> {

        @Override
        public Domain mapRow(ResultSet rs, int rowNum) throws SQLException {
            DomainProxy domain = proxyFactory.getObject();

            domain.setDomainId(rs.getLong("domain_id"));
            domain.setDomainName(rs.getString("domain_name"));
            domain.setAddressId(rs.getLong("address_id"));
            domain.setDomainTypeId(rs.getLong("domain_type_id"));

            return domain;
        }
    }
}
