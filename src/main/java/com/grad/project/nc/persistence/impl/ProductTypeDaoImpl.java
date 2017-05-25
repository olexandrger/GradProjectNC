package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.ProductType;
import com.grad.project.nc.model.proxy.ProductTypeProxy;
import com.grad.project.nc.persistence.AbstractDao;
import com.grad.project.nc.persistence.ProductTypeDao;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductTypeDaoImpl extends AbstractDao implements ProductTypeDao {

    private static final String PK_COLUMN_NAME = "product_type_id";

    private final ObjectFactory<ProductTypeProxy> proxyFactory;

    @Autowired
    public ProductTypeDaoImpl(JdbcTemplate jdbcTemplate, ObjectFactory<ProductTypeProxy> proxyFactory) {
        super(jdbcTemplate);
        this.proxyFactory = proxyFactory;
    }

    @Override
    public ProductType add(ProductType productType) {
        String insertQuery = "INSERT INTO \"product_type\" (\"product_type_name\", \"product_type_description\", " +
                "\"is_active\") VALUES (?, ?, ?)";

        Long productTypeId = executeInsertWithId(insertQuery, PK_COLUMN_NAME, productType.getProductTypeName(),
                productType.getProductTypeDescription(), productType.getIsActive());

        productType.setProductTypeId(productTypeId);

        return productType;
    }

    @Override
    public ProductType find(Long id) {
        String findOneQuery = "SELECT \"product_type_id\", \"product_type_name\", \"product_type_description\", " +
                "\"is_active\" FROM \"product_type\" WHERE \"product_type_id\" = ?";

        return findOne(findOneQuery, new ProductTypeRowMapper(), id);
    }

    @Override
    public ProductType update(ProductType productType) {
        String updateQuery = "UPDATE \"product_type\" SET \"product_type_name\"=?, " +
                "\"product_type_description\"=?, \"is_active\"=? " +
                "WHERE \"product_type_id\"=?";

        executeUpdate(updateQuery, productType.getProductTypeName(), productType.getProductTypeDescription(),
                productType.getIsActive(), productType.getProductTypeId());

        return productType;
    }

    @Override
    public void delete(Long id) {
        String deleteQuery = "DELETE FROM \"product_type\" WHERE \"product_type_id\" = ?";

        executeUpdate(deleteQuery);
    }

    @Override
    public List<ProductType> findAll() {
        String findAllQuery = "SELECT \"product_type_id\", \"product_type_name\", \"product_type_description\", " +
                "\"is_active\" FROM \"product_type\"";

        return findMultiple(findAllQuery, new ProductTypeRowMapper());
    }

    @Override
    public List<ProductType> findByNameContaining(String productTypeName) {
        String findQuery = "SELECT \"product_type_id\", \"product_type_name\", \"product_type_description\", " +
                "\"is_active\" FROM \"product_type\" WHERE LOWER(\"product_type_name\") LIKE LOWER(?)";

        String substr = "%" + productTypeName + "%";

        return findMultiple(findQuery, new ProductTypeRowMapper(), substr);
    }

    @Override
    public List<ProductType> findFirstN(int n) {
        String findQuery = "SELECT \"product_type_id\", \"product_type_name\", \"product_type_description\", " +
                "\"is_active\" FROM \"product_type\" ORDER BY \"product_type_id\" LIMIT ?";

        return findMultiple(findQuery, new ProductTypeRowMapper(), n);
    }

    @Override
    public List<ProductType> findLastN(int n) {
        String findQuery = "SELECT \"product_type_id\", \"product_type_name\", \"product_type_description\", " +
                "\"is_active\" FROM \"product_type\" ORDER BY \"product_type_id\" DESC LIMIT ?";

        return findMultiple(findQuery, new ProductTypeRowMapper(), n);
    }

    @Override
    public List<ProductType> findByActiveStatus(boolean isActive) {
        String findQuery = "SELECT \"product_type_id\", \"product_type_name\", \"product_type_description\", " +
                "\"is_active\" FROM \"product_type\" WHERE \"is_active\" = ?";

        return findMultiple(findQuery, new ProductTypeRowMapper(), isActive);
    }

    @Override
    public List<ProductType> findPaginated(int page, int amount) {
        int offset = (page - 1) * amount;
        String findPaginatedQuery = "SELECT \"product_type_id\", \"product_type_name\", \"product_type_description\", " +
                "\"is_active\" FROM \"product_type\" ORDER BY \"product_type_id\" DESC LIMIT ? OFFSET ?";

        return findMultiple(findPaginatedQuery, new ProductTypeRowMapper(), amount, offset);
    }

    @Override
    public ProductType findByProductId(Long productId) {
        String query = "SELECT pt.\"product_type_id\", pt.\"product_type_name\", " +
                "pt.\"product_type_description\", pt.\"is_active\" " +
                "FROM \"product_type\" pt " +
                "INNER JOIN \"product\" p " +
                "ON p.\"product_type_id\" = pt.\"product_type_id\" " +
                "WHERE p.\"product_id\" = ?";

        return findOne(query, new ProductTypeRowMapper(), productId);
    }

    @Override
    public int countTotalProducts() {
        String countQuery = "SELECT count(*) FROM \"product_type\"";
        return findOne(countQuery, new SingleColumnRowMapper<>(int.class));
    }

    private class ProductTypeRowMapper implements RowMapper<ProductType> {

        @Override
        public ProductType mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductTypeProxy productType = proxyFactory.getObject();

            productType.setProductTypeId(rs.getLong("product_type_id"));
            productType.setProductTypeName(rs.getString("product_type_name"));
            productType.setProductTypeDescription(rs.getString("product_type_description"));
            productType.setIsActive(rs.getBoolean("is_active"));

            return productType;
        }
    }
}
