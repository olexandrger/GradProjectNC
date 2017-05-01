package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Discount;
import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductRegionPrice;
import com.grad.project.nc.model.Region;
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
 * Created by Roman Savuliak on 26.04.2017.
 */
@Repository
public class ProductRegionPriceDao implements CrudDao<ProductRegionPrice> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public ProductRegionPrice add(ProductRegionPrice productRegionPrice) {
        SimpleJdbcInsert insertProductRegionPriceQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product_region_price")
                .usingGeneratedKeyColumns("price_id");

        Map<String, Object> parameters = new HashMap<String, Object>(3);
        parameters.put("product_id", productRegionPrice.getProductId());
        parameters.put("region_id", productRegionPrice.getRegionID());
        parameters.put("price", productRegionPrice.getPrice());

        Number newId = insertProductRegionPriceQuery.executeAndReturnKey(parameters);
        productRegionPrice.setPriceId(newId.longValue());
        return productRegionPrice;
    }

    @Override
    @Transactional
    public ProductRegionPrice update(ProductRegionPrice productRegionPrice) {
        final String UPDATE_QUERY = "UPDATE product_region_price SET" +
                " product_id = ?" +
                ", region_id = ?" +
                ", price = ? " +
                "WHERE price_id = ? ";

        jdbcTemplate.update(UPDATE_QUERY, new Object[]{
                productRegionPrice.getProductId()
                , productRegionPrice.getRegionID()
                , productRegionPrice.getPrice()
                , productRegionPrice.getPriceId()});

        return productRegionPrice;
    }

    @Override
    public ProductRegionPrice find(long id) {
        final String SELECT_QUERY = "SELECT price_id, product_id, region_id, price FROM product_region_price WHERE price_id = ?";
        ProductRegionPrice productRegionPrice = null;
        try {
            productRegionPrice = jdbcTemplate.queryForObject(SELECT_QUERY, new Object[]{id}, new ProductRegionPriceRowMapper());
        } catch (EmptyResultDataAccessException ex){

        }

        return productRegionPrice;
    }

    @Override
    public Collection<ProductRegionPrice> findAll() {
        final String SELECT_QUERY = "SELECT price_id, product_id, region_id, price FROM product_region_price";
        return jdbcTemplate.query(SELECT_QUERY, new ProductRegionPriceRowMapper());
    }

    @Override
    public void delete(ProductRegionPrice productRegionPrice) {
        final String DELETE_QUERY = "DELETE FROM product_region_price WHERE price_id = ?";

        jdbcTemplate.update(DELETE_QUERY, productRegionPrice.getPriceId());
    }

    public Collection<ProductRegionPrice> getProductRegionPricesByDiscount(Discount discount){

    }


    private static final class ProductRegionPriceRowMapper implements RowMapper<ProductRegionPrice> {
        @Override
        public ProductRegionPrice mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductRegionPrice productRegionPrice = new ProductRegionPrice();

            productRegionPrice.setPriceId(rs.getLong("price_id"));
            productRegionPrice.setProductId(rs.getLong("product_id"));
            productRegionPrice.setRegionID(rs.getLong("region_id"));
            productRegionPrice.setPrice(rs.getDouble("price"));

            return productRegionPrice;
        }
    }
}
