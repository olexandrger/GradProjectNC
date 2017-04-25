package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Discount;
import com.grad.project.nc.persistence.mappers.DiscountRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roman Savuliak on 25.04.2017.
 */
@Component
public class DiscountDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void insertDiscount(Discount discount){

        SimpleJdbcInsert insertDiscountQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("discount")
                .usingGeneratedKeyColumns("discount_id");

        Map<String, Object> parameters = new HashMap<String, Object>(4);
        parameters.put("discount_title", discount.getDiscountTitle());
        parameters.put("discount", discount.getDiscount());
        parameters.put("start_date", discount.getStartDate());
        parameters.put("end_date", discount.getEndDate());

        Number newId = insertDiscountQuery.executeAndReturnKey(parameters);
        discount.setDiscountId(newId.intValue());

    }

    @Transactional
    public Discount readDiscountById(int id){
        final String SELECT_QUERY = "SELECT discount_id, discount_title, discount, start_date, end_date FROM end_date WHERE discount_id = ?";

        Discount discount = jdbcTemplate.queryForObject(SELECT_QUERY, new Object[]{id}, new DiscountRowMapper());
        return discount;
    }

    @Transactional
    public void updateDiscount(Discount discount) {
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
    }

    @Transactional
    public void deleteDiscountById(int id) {

        final String DELETE_QUERY = "DELETE FROM discount WHERE discount_id = ?";

        jdbcTemplate.update(DELETE_QUERY,id);

    }

}
