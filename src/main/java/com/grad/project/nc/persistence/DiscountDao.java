package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Discount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roman Savuliak on 25.04.2017.
 */
@Repository
public class DiscountDao implements CrudDao<Discount>{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public Discount add(Discount discount) {

        SimpleJdbcInsert insertDiscountQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("discount")
                .usingGeneratedKeyColumns("discount_id");

        Map<String, Object> parameters = new HashMap<String, Object>(4);
        parameters.put("discount_title", discount.getDiscountTitle());
        parameters.put("discount", discount.getDiscount());
        parameters.put("start_date", discount.getStartDate());
        parameters.put("end_date", discount.getEndDate());

        Number newId = insertDiscountQuery.executeAndReturnKey(parameters);
        discount.setDiscountId(newId.longValue());
        return discount;
    }

    @Override
    @Transactional
    public Discount update(Discount discount) {
        final String UPDATE_QUERY = "UPDATE discount SET" +
                " discount_title = ?" +
                ", discount = ?" +
                ", start_date = ? " +
                ", end_date = ? " +
                "WHERE discount_id = ? ";

        jdbcTemplate.update(UPDATE_QUERY, new Object[]{
                discount.getDiscountTitle()
                ,discount.getDiscount()
                ,discount.getStartDate()
                ,discount.getEndDate()
                ,discount.getEndDate()});

        return discount;
    }

    @Override
    @Transactional
    public Discount find(long id) {
        final String SELECT_QUERY = "SELECT discount_id, discount_title, discount, start_date, end_date FROM discount WHERE discount_id = ?";
        Discount discount = null;
        try {
            discount = jdbcTemplate.queryForObject(SELECT_QUERY, new Object[]{id}, new DiscountRowMapper());
        } catch (EmptyResultDataAccessException ex){

        }

        return discount;
    }

    @Override
    @Transactional
    public Collection<Discount> findAll() {
        final String SELECT_QUERY = "SELECT discount_id, discount_title, discount, start_date, end_date FROM discount";

        return jdbcTemplate.query(SELECT_QUERY, new DiscountRowMapper());
    }

    @Override
    @Transactional
    public void delete(Discount discount) {

        final String DELETE_QUERY = "DELETE FROM discount WHERE discount_id = ?";

        jdbcTemplate.update(DELETE_QUERY, discount.getDiscountId());

    }

    private static final class DiscountRowMapper implements RowMapper<Discount> {
        @Override
        public Discount mapRow(ResultSet rs, int rowNum) throws SQLException {
            Discount discount = new Discount();

            discount.setDiscountId(rs.getLong("discount_id"));
            discount.setDiscountTitle(rs.getString("discount_title"));
            discount.setDiscount(rs.getDouble("discount"));
            discount.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());

            if (rs.getTimestamp("end_date") != null) {
                discount.setEndDate(rs.getTimestamp("end_date").toLocalDateTime());
            }

            return discount;
        }
    }

}
