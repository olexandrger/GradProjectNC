package com.grad.project.nc.controller.api.admin;

import com.grad.project.nc.controller.api.dto.FrontendProduct;
import com.grad.project.nc.model.Product;
import com.grad.project.nc.service.product.ProductService;
import com.grad.project.nc.support.validation.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductsController {

    private final ProductService productService;
    private final ProductValidator productValidator;

    @Autowired
    public AdminProductsController(ProductService productService, ProductValidator productValidator) {
        this.productService = productService;
        this.productValidator = productValidator;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, String> update(@RequestBody FrontendProduct frontendProduct) {
        productValidator.validate(frontendProduct);

        Product product = frontendProduct.toModel();
        productService.update(product);

        return new HashMap<String, String>() {{
            put("status", "success");
            put("message", "Product was successfully updated");
            put("id", product.getProductId().toString());
        }};
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Map<String, String> add(@RequestBody FrontendProduct frontendProduct) {
        productValidator.validate(frontendProduct);

        Product product = frontendProduct.toModel();
        productService.add(product);

        return new HashMap<String, String>() {{
            put("status", "success");
            put("message", "Product was successfully added");
            put("id", product.getProductId().toString());
        }};
    }
}
