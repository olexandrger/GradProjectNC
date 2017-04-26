package com.grad.project.nc.persistence;

import com.grad.project.nc.model.DiscountPrice;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DiscountPriceDao implements CrudDao<DiscountPrice>{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public DiscountPrice add(DiscountPrice discountPrice) {
        SimpleJdbcInsert insertDiscountPriceQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("discount_price")
                .usingGeneratedKeyColumns("discount_id");

        Map<String, Object> parameters = new HashMap<String, Object>(1);
        parameters.put("price_id", discountPrice.getPriceId());

        Number newId = insertDiscountPriceQuery.executeAndReturnKey(parameters);
        discountPrice.setDiscountId(newId.longValue());

        return discountPrice;
    }

    @Override
    @Transactional
    public DiscountPrice update(DiscountPrice discountPrice) {
        final String UPDATE_QUERY = "UPDATE discount_price SET price_id = ?" + "WHERE discount_id = ?";
        jdbcTemplate.update(UPDATE_QUERY, new Object[]{discountPrice.getPriceId(), discountPrice.getDiscountId()});
        return discountPrice;    }

    @Override
    @Transactional
    public DiscountPrice find(long id) {
        final String SELECT_QUERY = "SELECT discount_id, price_id FROM discount_price WHERE discount_id = ?";
        DiscountPrice discountPrice = jdbcTemplate.queryForObject(SELECT_QUERY, new Object[]{id}, new DiscountPriceRowMapper());
        return discountPrice;
    }

    @Override
    @Transactional
    public Collection<DiscountPrice> findAll() {
        final String SELECT_QUERY = "SELECT discount_id, price_id FROM discount_price";
        return jdbcTemplate.query(SELECT_QUERY, new DiscountPriceRowMapper());
    }

    @Override
    public void delete(DiscountPrice discountPrice) {
        final String DELETE_QUERY = "DELETE FROM discount_price WHERE discount_id = ?";
        jdbcTemplate.update(DELETE_QUERY, discountPrice.getDiscountId());
    }

    private static final class DiscountPriceRowMapper implements RowMapper<DiscountPrice> {
        @Override
        public DiscountPrice mapRow(ResultSet rs, int rowNum) throws SQLException {
            DiscountPrice discountPrice = new DiscountPrice();

            discountPrice.setDiscountId(rs.getLong("discount_id"));
            discountPrice.setPriceId(rs.getLong("price_id"));

            return discountPrice;
        }
    }
}
