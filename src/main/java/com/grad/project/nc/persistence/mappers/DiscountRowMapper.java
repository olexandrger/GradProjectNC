package com.grad.project.nc.persistence.mappers;

import com.grad.project.nc.model.Discount;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Roman Savuliak on 25.04.2017.
 */
public class DiscountRowMapper implements RowMapper<Discount> {
    @Override
    public Discount mapRow(ResultSet rs, int rowNum) throws SQLException {
        Discount discount = new Discount();

        discount.setDiscountId(rs.getInt("discount_id"));
        discount.setDiscountTitle(rs.getString("discount_title"));
        discount.setDiscount(rs.getDouble("discount"));
        discount.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
        discount.setEndDate(rs.getTimestamp("end_date").toLocalDateTime());

        return discount;
    }
}
