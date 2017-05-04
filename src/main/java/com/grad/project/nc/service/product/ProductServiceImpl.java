package com.grad.project.nc.service.product;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.persistence.CrudDao;
import com.grad.project.nc.persistence.ProductRegionPriceDao;
import com.grad.project.nc.persistence.impl.ProductCharacteristicValueDaoImpl;
import com.grad.project.nc.persistence.ProductDao;
import com.grad.project.nc.persistence.impl.ProductRegionPriceDaoImpl;
import com.grad.project.nc.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends AbstractService<Product> implements ProductService {

    private ProductDao productDao;
    private ProductRegionPriceDao productRegionPriceDao;

    @Autowired
    public ProductServiceImpl(ProductDao productDao,
                              ProductRegionPriceDaoImpl productRegionPriceDao) {
        this.productDao = productDao;
        this.productRegionPriceDao = productRegionPriceDao;
    }

    @Override
    public Product add(Product product) {
        productDao.add(product);
        productRegionPriceDao.addBatch(product.getPrices());
        return null;

    }

    @Override
    public CrudDao<Product> getBackingDao() {
        return productDao;
    }
}
