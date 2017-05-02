package com.grad.project.nc.controller.api.admin;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequestMapping("/api/admin/products")
@Slf4j
public class ProductsController {

    private ProductDao productDao;

    @Autowired
    public ProductsController(ProductDao productDao) {
        this.productDao = productDao;

    }

    @RequestMapping("/all")
    private Collection<Product> getAll() {
        return productDao.findAll();
    }

}
