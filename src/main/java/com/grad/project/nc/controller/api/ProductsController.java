package com.grad.project.nc.controller.api;

import com.grad.project.nc.controller.api.dto.FrontendProduct;
import com.grad.project.nc.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/products")
public class ProductsController {

    private final ProductService productService;

    @Autowired
    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public FrontendProduct getById(@PathVariable("id") Long id) {
        return FrontendProduct.fromEntity(productService.find(id));
    }

    @RequestMapping(value = "/byRegion/{id}", method = RequestMethod.GET)
    public Collection<FrontendProduct> getByRegion(@PathVariable("id") Long id) {
        return productService.findActiveProductsByRegionId(id)
                .stream()
                .map(FrontendProduct::fromEntity)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Collection<FrontendProduct> getAll() {
        return productService.findAll()
                .stream()
                .map(FrontendProduct::fromEntity)
                .collect(Collectors.toList());
    }
}
