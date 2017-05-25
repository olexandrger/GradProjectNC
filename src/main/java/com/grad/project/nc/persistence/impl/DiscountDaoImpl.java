package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.Discount;
import com.grad.project.nc.model.ProductRegionPrice;
import com.grad.project.nc.model.proxy.DiscountProxy;
import com.grad.project.nc.persistence.AbstractDao;
import com.grad.project.nc.persistence.DiscountDao;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DiscountDaoImpl extends AbstractDao implements DiscountDao {

    private static final String PK_COLUMN_NAME = "discount_id";

    private final ObjectFactory<DiscountProxy> proxyFactory;

    @Autowired
    public DiscountDaoImpl(JdbcTemplate jdbcTemplate, ObjectFactory<DiscountProxy> proxyFactory) {
        super(jdbcTemplate);
        this.proxyFactory = proxyFactory;
    }

    @Override
    @Transactional
    public Discount add(Discount discount) {
        String insertQuery = "INSERT INTO \"discount\" (\"discount_title\", \"discount\", " +
                "\"start_date\", \"end_date\") VALUES (?, ?, ?, ?)";

        Long discountId = executeInsertWithId(insertQuery, PK_COLUMN_NAME, discount.getDiscountTitle(),
                discount.getDiscount(), discount.getStartDate(), discount.getEndDate());

        discount.setDiscountId(discountId);

        deleteDiscountProductPrices(discountId);
        persistDiscountProductPrices(discount);



        return discount;
    }

    @Override
    @Transactional
    public Discount update(Discount discount) {
        String updateQuery = "UPDATE \"discount\" SET \"discount_title\" = ?, \"discount\" = ?, " +
                "\"start_date\" = ?, \"end_date\" = ? WHERE \"discount_id\" = ?";

        executeUpdate(updateQuery, discount.getDiscountTitle(), discount.getDiscount(),
                discount.getStartDate(), discount.getEndDate(), discount.getDiscountId());

        deleteDiscountProductPrices(discount.getDiscountId());
        persistDiscountProductPrices(discount);


        return discount;
    }

    @Override
    public void deleteDiscountProductPrices(Long discountId) {
        String deleteQuery = "DELETE FROM \"discount_price\" WHERE \"discount_id\" = ?";

        executeUpdate(deleteQuery, discountId);
    }


    @Override
    @Transactional
    public void persistDiscountProductPrices(Discount discount) {
        String insertQuery = "INSERT INTO \"discount_price\" (\"discount_id\", \"price_id\") VALUES(?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (ProductRegionPrice price : discount.getProductRegionPrices()) {
            batchArgs.add(new Object[]{discount.getDiscountId(), price.getPriceId()});
        }

        batchUpdate(insertQuery, batchArgs);
    }

    @Override
    public Discount findLargestDiscountByPriceId(Long priceId) {
        String findQuery = "SELECT d.\"discount_id\", d.\"discount_title\", d.\"discount\", " +
                "d.\"start_date\", d.\"end_date\" FROM \"discount\" d " +
                "JOIN \"discount_price\" dp " +
                "ON d.\"discount_id\" = dp.\"discount_id\" AND dp.\"price_id\" = ? " +
                "WHERE d.\"start_date\" < NOW() AND d.\"end_date\" > NOW() " +
                "ORDER BY d.\"discount\" DESC LIMIT 1";

        return findOne(findQuery, new DiscountRowMapper(), priceId);
    }

    @Override
    public Discount find(Long id) {
        String findOneQuery = "SELECT \"discount_id\", \"discount_title\", \"discount\", " +
                "\"start_date\", \"end_date\" FROM \"discount\" WHERE \"discount_id\" = ?";

        return findOne(findOneQuery, new DiscountRowMapper(), id);
    }

    @Override
    public List<Discount> findAll() {
        String findAllQuery = "SELECT \"discount_id\", \"discount_title\", \"discount\", " +
                "\"start_date\", \"end_date\" FROM \"discount\"";

        return findMultiple(findAllQuery, new DiscountRowMapper());
    }

    @Override
    public List<Discount> findAll(long size, long offset) {
        String findAllQuery = "SELECT \"discount_id\", \"discount_title\", \"discount\", " +
                "\"start_date\", \"end_date\" FROM \"discount\"";

        return findMultiplePage(findAllQuery, new DiscountRowMapper(), size, offset);
    }

    @Override
    public void delete(Long id) {
        String deleteQuery = "DELETE FROM \"discount\" WHERE \"discount_id\" = ?";

        executeUpdate(deleteQuery, id);
    }

    @Override
    public List<Discount> findByProductRegionPriceId(Long productRegionPriceId) {
        String query = "SELECT d.\"discount_id\", d.\"discount_title\", d.\"discount\", d.\"start_date\", " +
                "d.\"end_date\" " +
                "FROM \"discount\" d " +
                "INNER JOIN \"discount_price\" dp " +
                "ON d.\"discount_id\" = dp.\"discount_id\" " +
                "WHERE dp.\"price_id\" = ?";

        return findMultiple(query, new DiscountRowMapper(), productRegionPriceId);
    }

    private class DiscountRowMapper implements RowMapper<Discount> {

        @Override
        public Discount mapRow(ResultSet rs, int rowNum) throws SQLException {
            DiscountProxy discount = proxyFactory.getObject();

            discount.setDiscountId(rs.getLong("discount_id"));
            discount.setDiscountTitle(rs.getString("discount_title"));
            discount.setDiscount(rs.getDouble("discount"));
            discount.setStartDate(OffsetDateTime
                    .ofInstant(rs.getTimestamp("start_date").toInstant(), ZoneId.systemDefault()));
            discount.setEndDate(OffsetDateTime
                    .ofInstant(rs.getTimestamp("end_date").toInstant(), ZoneId.systemDefault()));

            return discount;
        }
    }
}
