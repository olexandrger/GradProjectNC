package com.grad.project.nc.controller.api.admin;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/products")
public class ProductsController {


    @Data
    @NoArgsConstructor
    private class Product {

    }

    @Data
    @NoArgsConstructor
    private class Characteristic {

    }
}
