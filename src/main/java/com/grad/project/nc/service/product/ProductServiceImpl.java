package com.grad.project.nc.service.product;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductCharacteristicValue;
import com.grad.project.nc.model.ProductRegionPrice;
import com.grad.project.nc.persistence.CrudDao;
import com.grad.project.nc.persistence.ProductCharacteristicValueDao;
import com.grad.project.nc.persistence.ProductDao;
import com.grad.project.nc.persistence.ProductRegionPriceDao;
import com.grad.project.nc.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends AbstractService<Product> implements ProductService {

    private ProductDao productDao;
    private ProductRegionPriceDao productRegionPriceDao;
    private ProductCharacteristicValueDao productCharacteristicValueDao;

    @Autowired
    public ProductServiceImpl(ProductDao productDao,
                              ProductRegionPriceDao productRegionPriceDao,
                              ProductCharacteristicValueDao productCharacteristicValueDao) {
        this.productDao = productDao;
        this.productRegionPriceDao = productRegionPriceDao;
        this.productCharacteristicValueDao = productCharacteristicValueDao;
    }

    @Override
    public CrudDao<Product> getBackingDao() {
        return productDao;
    }

    @Override
    @Transactional
    public Product find(Long id) {
        Product product = productDao.find(id);
        product.getProductType();
        product.getProductCharacteristicValues();
        product.getProductCharacteristics();
        product.getPrices();

        return product;
    }

    @Override
    @Transactional
    public List<Product> findAll() {
        List<Product> products = productDao.findAll();
        products.forEach(p -> {
            p.getProductType();
            p.getProductCharacteristicValues();
            p.getProductCharacteristics();
            p.getPrices();
        });

        return products;
    }

    @Override
    @Transactional
    public Product add(Product product) {
        productDao.add(product);

        product.getPrices()
                .forEach(p -> p.setProduct(new Product(product.getProductId())));

        productRegionPriceDao.persistBatch(product.getPrices());

        product.getProductCharacteristicValues()
                .forEach(v -> v.setProduct(new Product(product.getProductId())));

        productCharacteristicValueDao.persistBatch(
                product.getProductCharacteristicValues());

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
        products.forEach(p -> {
            p.getProductType();
            p.getProductCharacteristicValues();
            p.getProductCharacteristics();
            p.getPrices();
        });

        return products;
    }

    @Override
    @Transactional
    public List<Product> findActiveProductsByRegionId(Long regionId) {
        List<Product> products = productDao.findActiveByRegionId(regionId);
        products.forEach(p -> {
            p.getProductType();
            p.getProductCharacteristicValues();
            p.getProductCharacteristics();
            p.getPrices();
        });

        return products;
    }
}
