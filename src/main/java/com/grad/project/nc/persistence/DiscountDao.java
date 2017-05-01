package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Discount;
import com.grad.project.nc.model.ProductRegionPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roman Savuliak on 25.04.2017.
 */
@Repository
public class DiscountDao extends AbstractDao<Discount> {

    private ProductRegionPriceDao productRegionPriceDao;

    private DiscountRowMapper mapper = new DiscountRowMapper();

    @Autowired
    public DiscountDao(JdbcTemplate jdbcTemplate, ProductRegionPriceDao productRegionPriceDao) {
        super(jdbcTemplate);
        this.productRegionPriceDao = productRegionPriceDao;
    }

    @Override
    public Discount add(Discount discount) {

        KeyHolder keyHolder = executeInsert(connection -> {
            final String INSERT_QUERY =
                    "INSERT " +
                            "INTO discount ( " +
                            "discount.discount_title, " +
                            "discount.discount, " +
                            "discount.start_date, " +
                            "discount.end_date) " +
                            "VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
            preparedStatement.setString(1, discount.getDiscountTitle());
            preparedStatement.setDouble(2, discount.getDiscount());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(discount.getStartDate()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(discount.getEndDate()));
            return preparedStatement;
        });

        return find(getLongValue(keyHolder, "discount_id"));
    }

    @Override
    @Transactional
    public Discount update(Discount discount) {

        executeUpdate(connection -> {
            final String UPDATE_QUERY =
                    "UPDATE discount SET" +
                            " discount_title = ?" +
                            ", discount = ?" +
                            ", start_date = ? " +
                            ", end_date = ? " +
                            "WHERE discount_id = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setString(1, discount.getDiscountTitle());
            preparedStatement.setDouble(2, discount.getDiscount());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(discount.getStartDate()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(discount.getEndDate()));
            return preparedStatement;
        });

        saveDiscountPrise(discount);
        return discount;
    }

    @Override
    public Discount find(long id) {
        return findOne(connection -> {
            final String SELECT_QUERY =
                    "SELECT " +
                            "discount_id, " +
                            "discount_title, " +
                            "discount, " +
                            "start_date, " +
                            "end_date " +
                            "FROM discount " +
                            "WHERE discount_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, id);
            return preparedStatement;
        }, mapper);
    }

    @Override
    public Collection<Discount> findAll() {
        return findMultiple(connection -> {
            final String SELECT_QUERY =
                    "SELECT " +
                            "discount_id, " +
                            "discount_title, " +
                            "discount, " +
                            "start_date, " +
                            "end_date " +
                            "FROM discount ";
            return connection.prepareStatement(SELECT_QUERY);

        }, mapper);
    }

    @Override
    public void delete(Discount discount) {

        executeUpdate(connection -> {
            final String DELETE_QUERY =
                    "DELETE " +
                            "FROM discount " +
                            "WHERE discount_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setLong(1, discount.getDiscountId());
            return preparedStatement;
        });
    }

    private void deleteProductRegoinPrises(Discount discount){
        executeUpdate(connection -> {
            final String DELETE_QUERY =
                    "DELETE " +
                            "FROM discount_price " +
                            "WHERE discount_price.discount_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setLong(1, discount.getDiscountId());
            return preparedStatement;
        });
    }

    private void saveDiscountPrise(Discount discount){
        if(discount.getProductRegionPrices()!=null && discount.getProductRegionPrices().size()>0){
            deleteProductRegoinPrises(discount);
            executeUpdate(connection -> {
                String INSERT_QUERY =
                        "INSERT " +
                                "INTO discount_price (" +
                                "discount_price.discount_id, " +
                                "discount_price.price_id) " +
                                "VALUES (?, ?)";
                for(int i = 1; i<discount.getProductRegionPrices().size(); i++){
                    INSERT_QUERY+=", (?, ?)";
                }
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
                int i =0;
                for(ProductRegionPrice productRegionPrice:discount.getProductRegionPrices()){
                    preparedStatement.setLong(++i, discount.getDiscountId());
                    preparedStatement.setLong(++i, productRegionPrice.getPriceId());
                }
                return preparedStatement;
            });

        } else {
            deleteProductRegoinPrises(discount);
        }
    }

    public Collection<Discount> findByProductRegionPrise(ProductRegionPrice productRegionPrice){

    }

    private class DiscountProxy extends Discount{
        @Override
        public Collection<ProductRegionPrice> getProductRegionPrices() {
            if(super.getProductRegionPrices() == null){
              setProductRegionPrices(productRegionPriceDao.getProductRegionPricesByDiscount(this));
            }
            return super.getProductRegionPrices();
        }
    }

    private final class DiscountRowMapper implements RowMapper<Discount> {
        @Override
        public Discount mapRow(ResultSet rs, int rowNum) throws SQLException {
            Discount discount = new DiscountProxy();

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
