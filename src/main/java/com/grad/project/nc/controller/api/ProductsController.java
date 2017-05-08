package com.grad.project.nc.controller.api;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.persistence.ProductDao;
import com.grad.project.nc.persistence.RegionDao;
import com.grad.project.nc.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/products")
public class ProductsController {

    private ProductDao productDao;
    private ProductService productService;


    @Autowired
    public ProductsController(ProductDao productDao, ProductService productService) {
        this.productDao = productDao;
        this.productService = productService;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Product getById(@PathVariable("id") Long id) {
        return productService.find(id);
    }

    @RequestMapping(value = "/byRegion/{id}")
    public Collection<Product> getByRegion(@PathVariable("id") Long id) {
        //TODO move to controller
        return productDao.findByRegionId(id).stream().filter(Product::getIsActive).collect(Collectors.toList());
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Collection<Product> getAll() {
        return productDao.findAll();
    }
}
