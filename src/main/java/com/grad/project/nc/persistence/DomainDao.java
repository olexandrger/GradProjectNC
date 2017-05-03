package com.grad.project.nc.persistence;

import com.grad.project.nc.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

@Repository
public class DomainDao extends AbstractDao<Domain> {

    AddressDao addressDao;
    CategoryDao categoryDao;
    UserDao userDao;

    @Autowired
    public DomainDao(JdbcTemplate jdbcTemplate, CategoryDao categoryTypeDao, AddressDao addressDao) {
        super(jdbcTemplate);
        this.addressDao = addressDao;
        this.categoryDao = categoryTypeDao;
        //this.userDao = userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Domain add(Domain domain) {

        KeyHolder keyHolder = executeInsert(connection -> {
            String stretment = "INSERT INTO \"domain\"( domain_name, address_id, domain_type_id) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(stretment, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, domain.getDomainName());
            preparedStatement.setLong(2, domain.getAddress().getAddressId());
            preparedStatement.setLong(3, domain.getDomainType().getCategoryId());
            return preparedStatement;

        });

        return find(getLongValue(keyHolder, "domain_id"));
    }

    @Override
    public Domain update(Domain entity) {

        executeUpdate(connection -> {
            final String UPDATE_QUERY =
                    "UPDATE " +
                            "domain " +
                            "SET " +
                            "domain_name = ?, " +
                            "address_id = ? , " +
                            "domain_type_id = ? " +
                            "WHERE domain_id = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setString(1, entity.getDomainName());
            preparedStatement.setLong(2, entity.getAddress().getAddressId());
            preparedStatement.setLong(3, entity.getDomainType().getCategoryId());
            preparedStatement.setLong(4, entity.getDomainId());

            return preparedStatement;

        });
        saveAllUsers(entity);

        return entity;
    }

    @Override
    public Domain find(Long id) {

        return findOne(connection -> {
            final String SELECT_QUERY = "SELECT " +
                    "domain_id, " +
                    "domain_name, " +
                    "address_id, " +
                    "domain_type_id " +
                    "FROM \"domain\" " +
                    "WHERE domain_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, id);
            return preparedStatement;
        }, new DomainRowMapper());

    }

    @Override
    public List<Domain> findAll() {
        String findAllQuery = "SELECT \"domain_id\", \"domain_name\", \"address_id\", \"domain_type_id\" " +
                "FROM \"domain\"";
        return query(findAllQuery, new DomainRowMapper());
    }

    @Override
    public void delete(Domain entity) {

        executeUpdate(connection -> {
            final String DELETE_QUERY = "DELETE FROM \"domain\" WHERE domain_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setLong(1, entity.getDomainId());
            return preparedStatement;
        });

    }

    Collection<Domain> getDomainsByUser(User user) {
        return findMultiple(connection -> {
            String query =
                    "SELECT " +
                            "\"domain\".domain_id, " +
                            "\"domain\".domain_name, " +
                            "\"domain\".address_id, " +
                            "\"domain\".domain_type_id " +
                            "FROM \"domain\" " +
                            "INNER JOIN user_domain " +
                            "ON \"domain\".domain_id = user_domain.domain_id " +
                            "WHERE user_domain.user_id = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, user.getUserId());

            return statement;
        }, new DomainRowMapper());
    }

    private void deleteDomainUsers(Domain domain){
        executeUpdate(connection -> {
            final String DELETE_ALL_QUERY =
                    "DELETE " +
                            "FROM user_domain " +
                            "WHERE domain_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL_QUERY);
            preparedStatement.setLong(1, domain.getDomainId());
            return preparedStatement;
        });
    }

    private void saveAllUsers(Domain domain){
        if(domain.getUsers() != null && domain.getUsers().size()>0){
            deleteDomainUsers(domain);
            executeUpdate(connection -> {
                String INSERT_QUERY = "INSERT INTO user_domain (user_domain.user_id, user_domain.domain_id) VALUES (?, ?)";
                for (int i = 1; i < domain.getUsers().size(); i++) {
                    INSERT_QUERY += ",(?, ?)";
                }
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
                int i =0;
                for(User user : domain.getUsers()){
                    preparedStatement.setLong(++i, user.getUserId());
                    preparedStatement.setLong(++i, domain.getDomainId());
                }
                return preparedStatement;
            });
        } else {
            deleteDomainUsers(domain);
        }

    }



    private class DomainProxy extends Domain {

        @Override
        public Address getAddress() {
            if (super.getAddress() == null) {
                super.setAddress(addressDao.getAddressByDomain(this));
            }
            return super.getAddress();
        }

        @Override
        public Category getDomainType() {
            if (super.getDomainType() == null) {
                super.setDomainType(categoryDao.findDomainType(this.getDomainId()));
            }
            return super.getDomainType();
        }

        @Override
        public Collection<User> getUsers() {
            if (super.getUsers() == null) {
                super.setUsers(userDao.findByDomain(this));
            }
            return super.getUsers();
        }
    }


    private final class DomainRowMapper implements RowMapper<Domain> {

        @Override
        public Domain mapRow(ResultSet rs, int rowNum) throws SQLException {
            Domain domain = new DomainProxy();
            domain.setDomainId(rs.getLong("domain_id"));
            domain.setDomainName(rs.getString("domain_name"));
            return domain;
        }
    }


}
