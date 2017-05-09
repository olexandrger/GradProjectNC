package com.grad.project.nc.controller.api;

import com.grad.project.nc.controller.api.dto.*;
import com.grad.project.nc.model.*;
import com.grad.project.nc.persistence.ProductDao;
import com.grad.project.nc.persistence.RegionDao;
import com.grad.project.nc.service.product.ProductService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
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
    public FrontendProduct getById(@PathVariable("id") Long id) {
        return FrontendProduct.fromEntity(productService.find(id));
    }

    @RequestMapping(value = "/byRegion/{id}", method = RequestMethod.GET)
    public Collection<FrontendProduct> getByRegion(@PathVariable("id") Long id) {
        return productDao.findByRegionId(id).stream().filter(Product::getIsActive).map(FrontendProduct::fromEntity).collect(Collectors.toList());
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Collection<FrontendProduct> getAll() {
        return productDao.findAll().stream().map(FrontendProduct::fromEntity).collect(Collectors.toList());
    }











}
