package com.grad.project.nc.controller.api.admin;

import com.grad.project.nc.controller.api.dto.FrontendProduct;
import com.grad.project.nc.model.Product;
import com.grad.project.nc.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/products")
@Slf4j
public class AdminProductsController {

    private final ProductService productService;

    @Autowired
    public AdminProductsController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, String> update(@RequestBody FrontendProduct frontendProduct) {
        Product product = frontendProduct.toModel();
        log.info(product.toString());
        productService.update(product);

        return new HashMap<String, String>() {{
            put("status", "success");
            put("message", "Product was successfully updated");
            put("id", product.getProductId().toString());
        }};
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Map<String, String> add(@RequestBody FrontendProduct frontendProduct) {
        Product product = frontendProduct.toModel();
        productService.add(product);

        return new HashMap<String, String>() {{
            put("status", "success");
            put("message", "Product was successfully added");
            put("id", product.getProductId().toString());
        }};
    }
}
