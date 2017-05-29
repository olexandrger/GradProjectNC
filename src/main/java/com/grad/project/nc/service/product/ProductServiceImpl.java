package com.grad.project.nc.service.product;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductCharacteristicValue;
import com.grad.project.nc.model.ProductRegionPrice;
import com.grad.project.nc.persistence.*;
import com.grad.project.nc.service.AbstractService;
import com.grad.project.nc.support.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends AbstractService<Product> implements ProductService {

    private final ProductDao productDao;
    private final ProductRegionPriceDao productRegionPriceDao;
    private final ProductCharacteristicValueDao productCharacteristicValueDao;

    private final DiscountDao discountDao;

    @Autowired
    public ProductServiceImpl(ProductDao productDao,
                              ProductRegionPriceDao productRegionPriceDao,
                              ProductCharacteristicValueDao productCharacteristicValueDao,
                              DiscountDao discountDao) {
        this.productDao = productDao;
        this.productRegionPriceDao = productRegionPriceDao;
        this.productCharacteristicValueDao = productCharacteristicValueDao;
        this.discountDao = discountDao;
    }

    @Override
    public CrudDao<Product> getBackingDao() {
        return productDao;
    }

    @Override
    @Transactional
    public Product find(Long id) {
        Product product = productDao.find(id);
        loadProductHelper(product);

        return product;
    }

    @Override
    @Transactional
    public List<Product> findAll() {
        List<Product> products = productDao.findAll();
        loadProductHelper(products);

        return products;
    }

    @Override
    @Transactional
    public Product findCatalogProduct(Long id) {
        Product product = productDao.find(id);
        loadProductHelper(product);

        product.getPrices().forEach(price ->
                price.setDiscounts(Collections.singletonList(discountDao
                        .findLargestDiscountByPriceId(price.getPriceId()))));

        return product;
    }

    @Override
    @Transactional
    public Page<Product> findPaginated(int page, int amount) {
        int totalPages = (int) Math.ceil(productDao.countTotalProducts() / (double) amount);
        List<Product> products = productDao.findPaginated(page, amount);
        loadProductHelper(products);

        return new Page<>(products, totalPages);
    }

    @Override
    @Transactional
    public List<Product> findLastN(int n) {
        return productDao.findLastN(n);
    }

    @Override
    @Transactional
    public List<Product> findByNameContaining(String productName) {
        return productDao.findByNameContaining(productName);
    }

    @Override
    @Transactional
    public Product add(Product product) {
        productDao.add(product);

        product.getPrices()
                .forEach(price -> price.setProduct(new Product(product.getProductId())));

        productRegionPriceDao.persistBatch(product.getPrices());

        product.getProductCharacteristicValues()
                .forEach(value -> value.setProduct(new Product(product.getProductId())));

        productCharacteristicValueDao.persistBatch(product.getProductCharacteristicValues());

        return product;
    }

    @Override
    @Transactional
    public Product update(Product product) {
        productDao.update(product);

        updateProductCharacteristicValues(product);
        updateProductPrices(product);

        return product;
    }

    private void updateProductCharacteristicValues(Product product) {
        List<ProductCharacteristicValue> valuesToBeDeleted = productCharacteristicValueDao
                .findByProductId(product.getProductId())
                .stream()
                .filter(v -> product.getProductCharacteristicValues()
                        .stream()
                        .noneMatch(pv -> v.getValueId().equals(pv.getValueId())))
                .collect(Collectors.toList());

        productCharacteristicValueDao.deleteBatch(valuesToBeDeleted);

        productCharacteristicValueDao.updateBatch(product.getProductCharacteristicValues()
                .stream()
                .filter(v -> v.getValueId() != null)
                .collect(Collectors.toList())
        );

        productCharacteristicValueDao.persistBatch(product.getProductCharacteristicValues()
                .stream()
                .filter(v -> v.getValueId() == null)
                .collect(Collectors.toList())
        );
    }

    private void updateProductPrices(Product product) {
        List<ProductRegionPrice> pricesToBeDeleted = productRegionPriceDao
                .findByProductId(product.getProductId())
                .stream()
                .filter(p -> product.getPrices()
                        .stream()
                        .noneMatch(pp -> p.getPriceId().equals(pp.getPriceId())))
                .collect(Collectors.toList());
        productRegionPriceDao.deleteBatch(pricesToBeDeleted);

        productRegionPriceDao.updateBatch(product.getPrices()
                .stream()
                .filter(v -> v.getPriceId() != null)
                .collect(Collectors.toList())
        );

        productRegionPriceDao.persistBatch(product.getPrices()
                .stream()
                .filter(v -> v.getPriceId() == null)
                .collect(Collectors.toList())
        );
    }

    @Override
    @Transactional
    public List<Product> findByProductTypeId(Long productTypeId) {
        List<Product> products = productDao.findByProductTypeId(productTypeId);
        loadProductHelper(products);

        return products;
    }

    @Override
    @Transactional
    public List<Product> findCatalogProductsByRegionId(Long regionId) {
        List<Product> products = productDao.findActiveByRegionId(regionId);
        loadProductHelper(products);

        products.forEach(p -> p.getPrices()
                .forEach(price ->
                        price.setDiscounts(Collections.singletonList(discountDao
                                .findLargestDiscountByPriceId(price.getPriceId())))));

        return products;
    }

    @Override
    @Transactional
    public Page<Product> findActiveByProductTypeIdAndRegionIdPaginated(Long productTypeId, Long regionId,
                                                                       int page, int amount) {
        int totalPages = (int) Math.ceil(productDao.countActiveProductsOf(productTypeId, regionId) / (double) amount);
        List<Product> products = productDao.findActiveByProductTypeIdAndRegionIdPaginated(productTypeId, regionId,
                page, amount);
        loadProductHelper(products);

        products.forEach(p -> p.getPrices()
                .forEach(price ->
                        price.setDiscounts(Collections.singletonList(discountDao
                                .findLargestDiscountByPriceId(price.getPriceId())))));

        return new Page<>(products, totalPages);
    }

    @Override
    public List<Product> findByNameContaining(String productName, Long productTypeId, Long regionId) {
        return productDao.findByNameContaining(productName, productTypeId, regionId);
    }

    private void loadProductHelper(Product product) {
        product.getProductType();
        product.getProductCharacteristicValues();
        product.getProductCharacteristics();
        product.getPrices();
    }

    private void loadProductHelper(List<Product> products) {
        products.forEach(p -> {
            p.getProductType();
            p.getProductCharacteristicValues();
            p.getProductCharacteristics();
            p.getPrices();
        });
    }
}
