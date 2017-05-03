package com.grad.project.nc.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grad.project.nc.model.Discount;
import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductRegionPrice;
import com.grad.project.nc.model.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * reated by DeniG on 5/01/2017.
 */
@Repository
public class ProductRegionPriceDao extends AbstractDao<ProductRegionPrice> {

    ProductRegionPriceRowMapper mapper = new ProductRegionPriceRowMapper();

    private DiscountDao discountDao;
    private ProductDao productDao;
    private RegionDao regionDao;


    @Autowired
    public ProductRegionPriceDao(JdbcTemplate jdbcTemplate, /*  DiscountDao discountDao, ProductDao productDao, */RegionDao regionDao) {
        super(jdbcTemplate);
        //this.discountDao = discountDao;
        this.regionDao = regionDao;

    }


    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }
    public void setDiscountDao(DiscountDao discountDao) {
        this.discountDao = discountDao;
    }

    @Override
    public ProductRegionPrice add(ProductRegionPrice productRegionPrice) {

        KeyHolder keyHolder = executeInsert(connection -> {
            final String INSERT_QUERY =
                    "INSERT " +
                            "INTO product_region_price (" +
                            "price, " +
                            "product_id, " +
                            "region_id) " +
                            "VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
            preparedStatement.setDouble(1, productRegionPrice.getPrice());
            preparedStatement.setLong(2, productRegionPrice.getProduct().getProductId());
            preparedStatement.setLong(3, productRegionPrice.getRegion().getRegionId());
            return preparedStatement;
        });

        return find(getLongValue(keyHolder, "price_id"));
    }

    @Override
    @Transactional
    public ProductRegionPrice update(ProductRegionPrice productRegionPrice) {
        executeUpdate(connection -> {
            final String UPDATE_QUERY = "UPDATE product_region_price SET" +
                    " product_id = ?" +
                    ", region_id = ?" +
                    ", price = ? " +
                    "WHERE price_id = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setLong(1, productRegionPrice.getProduct().getProductId());
            preparedStatement.setLong(2, productRegionPrice.getRegion().getRegionId());
            preparedStatement.setDouble(3, productRegionPrice.getPrice());
            preparedStatement.setLong(4, productRegionPrice.getPriceId());
            return preparedStatement;
        });

        saveAllDiscounts(productRegionPrice);
        return productRegionPrice;
    }

    @Override
    public ProductRegionPrice find(long id) {
        return findOne(connection -> {
            final String SELECT_QUERY =
                    "SELECT " +
                            "price_id, " +
                            "price " +
                            "FROM product_region_price " +
                            "WHERE price_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, id);
            return preparedStatement;
        }, mapper);
    }

    @Override
    public Collection<ProductRegionPrice> findAll() {
        return findMultiple(connection -> {
            final String SELECT_QUERY =
                    "SELECT " +
                            "price_id, " +
                            "price " +
                            "FROM product_region_price";
            return connection.prepareStatement(SELECT_QUERY);
        }, mapper);
    }

    @Override
    public void delete(ProductRegionPrice productRegionPrice) {
        executeUpdate(connection -> {
            final String DELETE_QUERY =
                    "DELETE " +
                            "FROM product_region_price " +
                            "WHERE price_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setLong(1, productRegionPrice.getPriceId());
            return preparedStatement;
        });
    }

    public Collection<ProductRegionPrice> getProductRegionPricesByDiscount(Discount discount) {

        return findMultiple(connection -> {
            final String SELECT_QUERY =
                    "SELECT " +
                            "prp.price_id, " +
                            "price " +
                            "FROM product_region_price prp " +
                            "INNER JOIN discount_price dp " +
                            "ON prp.price_id=dp.price_id " +
                            "WHERE dp.discount_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, discount.getDiscountId());
            return preparedStatement;
        }, mapper);
    }

    private void deleteAllDiscounts(ProductRegionPrice productRegionPrice) {
        executeUpdate(connection -> {
            final String DELETE_QUERY =
                    "DELETE " +
                            "FROM discount_price " +
                            "WHERE discount_price.price_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setLong(1, productRegionPrice.getPriceId());
            return preparedStatement;
        });
    }

    private void saveAllDiscounts(ProductRegionPrice productRegionPrice) {
        if (productRegionPrice.getDiscounts() != null && productRegionPrice.getDiscounts().size() > 0) {
            deleteAllDiscounts(productRegionPrice);
            executeUpdate(connection -> {
                String INSERT_QUERY =
                        "INSERT " +
                                "INTO discount_price (" +
                                "discount_price.discount_id, " +
                                "discount_price.price_id) " +
                                "VALUES (?, ?)";
                for (int i = 1; i < productRegionPrice.getDiscounts().size(); i++) {
                    INSERT_QUERY += ", (?, ?)";
                }
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
                int i = 0;
                for (Discount discount : productRegionPrice.getDiscounts()) {
                    preparedStatement.setLong(++i, discount.getDiscountId());
                    preparedStatement.setLong(++i, productRegionPrice.getPriceId());
                }
                return preparedStatement;
            });
        } else {
            deleteAllDiscounts(productRegionPrice);
        }
    }

    public Collection<ProductRegionPrice> getPricesByProduct(Product product) {
        return findMultiple(connection -> {
            final String SELECT_QUERY =
                    "SELECT " +
                            "prp.price_id, " +
                            "prp.product_id, " +
                            "prp.region_id, " +
                            "prp.price " +
                            "FROM product_region_price prp " +
                            "WHERE prp.product_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, product.getProductId());
            return preparedStatement;
        }, mapper);
    }


    private class ProductRegionPriceProxy extends ProductRegionPrice {
        @Override
        @JsonIgnore
        public Product getProduct() {
            if (super.getProduct() == null) {
                super.setProduct(productDao.getProductByProductRegionPrise(this));
            }
            return super.getProduct();
        }

        @Override
        public Region getRegion() {
            if (super.getRegion() == null) {
                super.setRegion(regionDao.fingRegionByProductRegionPrice(this));
            }
            return super.getRegion();
        }

        @Override
        public Collection<Discount> getDiscounts() {
            if (super.getDiscounts() == null) {
                super.setDiscounts(discountDao.findByProductRegionPrise(this));
            }
            return super.getDiscounts();
        }
    }


    private final class ProductRegionPriceRowMapper implements RowMapper<ProductRegionPrice> {
        @Override
        public ProductRegionPrice mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductRegionPrice productRegionPrice = new ProductRegionPriceProxy();

            productRegionPrice.setPriceId(rs.getLong("price_id"));
            productRegionPrice.setPrice(rs.getDouble("price"));

            return productRegionPrice;
        }
    }
}
