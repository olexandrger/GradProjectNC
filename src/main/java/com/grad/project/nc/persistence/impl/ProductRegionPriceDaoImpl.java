package com.grad.project.nc.persistence.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grad.project.nc.model.Discount;
import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductRegionPrice;
import com.grad.project.nc.model.Region;
import com.grad.project.nc.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * created by DeniG on 5/01/2017.
 */
@Repository
public class ProductRegionPriceDaoImpl extends AbstractDao<ProductRegionPrice> implements ProductRegionPriceDao {

    private static final String PK_COLUMN_NAME = "price_id";

    private DiscountDao discountDao;
    private ProductDao productDao;
    private RegionDao regionDao;

    @Autowired
    public ProductRegionPriceDaoImpl(JdbcTemplate jdbcTemplate, /*  DiscountDao discountDao, ProductDao productDao, */RegionDao regionDao) {
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
        String insertQuery = "INSERT INTO \"product_region_price\" (\"product_id\", \"region_id\", \"price\") " +
                "VALUES(?,?,?)";

        Long prpId = executeInsertWithId(insertQuery, PK_COLUMN_NAME, productRegionPrice.getProduct().getProductId(),
                productRegionPrice.getRegion().getRegionId(), productRegionPrice.getPrice());

        productRegionPrice.setPriceId(prpId);

        return productRegionPrice;
    }

    @Override
    @Transactional
    public ProductRegionPrice update(ProductRegionPrice productRegionPrice) {
        String updateQuery = "UPDATE \"product_region_price\" SET \"product_id\"=?, \"region_id\"=?, " +
                "\"price\"=? WHERE \"price_id\"=?";

        executeUpdate(updateQuery, productRegionPrice.getProduct().getProductId(),
                productRegionPrice.getRegion().getRegionId(), productRegionPrice.getPrice(),
                productRegionPrice.getPriceId());

        return productRegionPrice;
    }

    @Override
    public ProductRegionPrice find(Long id) {
        String query = "SELECT \"price_id\", \"product_id\", \"region_id\", \"price\" " +
                "FROM \"product_region_price\" WHERE \"price_id\"=?";

        return findOne(query, new ProductRegionPriceRowMapper(), id);
    }

    @Override
    public List<ProductRegionPrice> findAll() {
        String findAllQuery = "SELECT \"price_id\", \"product_id\", \"region_id\", \"price\" " +
                "FROM \"product_region_price\"";

        return query(findAllQuery, new ProductRegionPriceRowMapper());
    }

    @Override
    public void delete(ProductRegionPrice productRegionPrice) {
        String deleteQuery = "DELETE FROM \"product_region_price\" WHERE \"price_id\"=?";
        executeUpdate(deleteQuery, productRegionPrice.getPriceId());
    }

    @Override
    public List<ProductRegionPrice> getProductRegionPricesByDiscount(Discount discount) {
        String query = "SELECT prp.\"price_id\", prp.\"product_id\", prp.\"region_id\", prp.\"price\" " +
                "FROM \"product_region_price\" prp " +
                "INNER JOIN \"discount_price\" dp " +
                "ON prp.\"price_id\"=dp.\"price_id\" " +
                "WHERE dp.\"discount_id\" = ?";


        return query(query, new ProductRegionPriceRowMapper(), discount.getDiscountId());
    }

    @Override
    public void deleteAllDiscounts(ProductRegionPrice productRegionPrice) {
        String query = "DELETE FROM \"discount_price\" WHERE \"price_id\" = ?";

        executeUpdate(query, productRegionPrice.getPriceId());
    }

    @Override
    public void saveAllDiscounts(ProductRegionPrice productRegionPrice) {
        deleteAllDiscounts(productRegionPrice);

        String insertQuery = "INSERT INTO \"discount_price\" (\"discount_id\", \"price_id\") VALUES (?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (Discount discount : productRegionPrice.getDiscounts()) {
            batchArgs.add(new Object[]{discount.getDiscountId(), productRegionPrice.getPriceId()});
        }
        batchUpdate(insertQuery, batchArgs);
    }

    @Override
    public void addBatch(List<ProductRegionPrice> prices) {
        String insertQuery = "INSERT INTO \"product_region_price\" (\"product_id\", \"region_id\", \"price\") " +
                "VALUES(?,?,?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (ProductRegionPrice price : prices) {
            batchArgs.add(new Object[]{price.getProduct().getProductId(),
                    price.getRegion().getRegionId(), price.getPrice()});
        }
        batchUpdate(insertQuery, batchArgs);
    }

    @Override
    public List<ProductRegionPrice> getPricesByProduct(Product product) {
        String query = "SELECT prp.\"price_id\", prp.\"product_id\", prp.\"region_id\", prp.\"price\" " +
                "FROM \"product_region_price\" prp " +
                "WHERE prp.\"product_id\"=?";

        return query(query, new ProductRegionPriceRowMapper(), product.getProductId());
    }


    private class ProductRegionPriceProxy extends ProductRegionPrice {

        private Long productId;
        private Long regionId;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Long getRegionId() {
            return regionId;
        }

        public void setRegionId(Long regionId) {
            this.regionId = regionId;
        }

        @Override
        @JsonIgnore
        public Product getProduct() {
            if (super.getProduct() == null) {
                super.setProduct(productDao.find(getProductId()));
            }
            return super.getProduct();
        }

        @Override
        public Region getRegion() {
            if (super.getRegion() == null) {
                super.setRegion(regionDao.find(getRegionId()));
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
            ProductRegionPriceProxy productRegionPrice = new ProductRegionPriceProxy();

            productRegionPrice.setPriceId(rs.getLong("price_id"));
            productRegionPrice.setProductId(rs.getLong("product_id"));
            productRegionPrice.setRegionId(rs.getLong("region_id"));
            productRegionPrice.setPrice(rs.getDouble("price"));

            return productRegionPrice;
        }
    }
}
