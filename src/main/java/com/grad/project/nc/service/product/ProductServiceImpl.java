package com.grad.project.nc.service.product;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.persistence.CrudDao;
import com.grad.project.nc.persistence.ProductCharacteristicValueDao;
import com.grad.project.nc.persistence.ProductDao;
import com.grad.project.nc.persistence.ProductRegionPriceDao;
import com.grad.project.nc.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends AbstractService<Product> implements ProductService {

    private ProductDao productDao;
    private ProductRegionPriceDao productRegionPriceDao;

    private ProductCharacteristicValueDao productCharacteristicValueDao;

    @Autowired
    public ProductServiceImpl(ProductDao productDao, ProductCharacteristicValueDao productCharacteristicValueDao,
                              ProductRegionPriceDao productRegionPriceDao) {
        this.productDao = productDao;
        this.productCharacteristicValueDao = productCharacteristicValueDao;
    }

    @Override
    public Product add(Product product) {
       return null;




    }

    @Override
    public CrudDao<Product> getBackingDao() {
        return productDao;
    }
}
