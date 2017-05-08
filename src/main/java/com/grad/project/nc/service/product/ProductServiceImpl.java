package com.grad.project.nc.service.product;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.persistence.CrudDao;
import com.grad.project.nc.persistence.ProductCharacteristicValueDao;
import com.grad.project.nc.persistence.ProductDao;
import com.grad.project.nc.persistence.ProductRegionPriceDao;
import com.grad.project.nc.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Product find(Long id) {
        Product product = productDao.find(id);
        product.getProductCharacteristicValues();
        product.getProductType();
        product.getProductCharacteristics();
        return product;
    }

    @Override
    public void delete(Product entity) {

    }

    @Override
    public Product add(Product product) {
        productDao.add(product);
        addPricesAndValues(product);
        return product;
    }

    @Override
    public Product update(Product product) {
        productDao.update(product);
        productRegionPriceDao.deleteByProductId(product.getProductId());
        productCharacteristicValueDao.deleteByProductId(product.getProductId());
        addPricesAndValues(product);
        return product;
    }

    @Transactional
    private void addPricesAndValues(Product product) {
        productRegionPriceDao.persistBatch(product.getProductId(), product.getPrices()
                .stream()
                .peek(price -> price.setProduct(product))
                .collect(Collectors.toList()));

        productCharacteristicValueDao.persistBatch(product.getProductId(), product.getProductCharacteristicValues()
                .stream()
                .peek(value -> value.setProduct(product))
                .collect(Collectors.toList()));
    }

    @Override
    public CrudDao<Product> getBackingDao() {
        return productDao;
    }
}
