package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Product;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Alex on 4/25/2017.
 */
@Component
public interface ProductDao {
    @Transactional
    public void insertProduct(Product product);
    @Transactional
    public Product readProductById(int id);
    @Transactional
    public void updateProduct(Product product);
    @Transactional
    public void deleteProductById(int id);

}
