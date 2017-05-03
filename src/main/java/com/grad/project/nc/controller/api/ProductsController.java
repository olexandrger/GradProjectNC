package com.grad.project.nc.controller.api;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.persistence.ProductDao;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user/products")
public class ProductsController {

    private ProductDao productDao;

    @Autowired
    public ProductsController(ProductDao productDao) {
        this.productDao = productDao;

    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    private Product getById(@PathVariable("id") Long id) {
        return productDao.find(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    private Collection<Product> getAll() {
        return productDao.findAll();
    }
}
