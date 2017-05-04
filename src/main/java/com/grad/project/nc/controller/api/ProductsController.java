package com.grad.project.nc.controller.api;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.persistence.ProductDao;
import com.grad.project.nc.persistence.RegionDao;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/products")
public class ProductsController {

    private ProductDao productDao;
    private RegionDao regionDao;


    @Autowired
    public ProductsController(ProductDao productDao, RegionDao regionDao) {
        this.regionDao = regionDao;
        this.productDao = productDao;

    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    private Product getById(@PathVariable("id") Long id) {
        return productDao.find(id);
    }

    @RequestMapping(value = "/byRegion/{id}")
    private Collection<Product> getByRegion(@PathVariable("id") Long id) {
        //TODO move to controller
        return productDao.findProductsByRegion(regionDao.find(id)).stream().filter(Product::getIsActive).collect(Collectors.toList());
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    private Collection<Product> getAll() {
        return productDao.findAll();
    }
}
