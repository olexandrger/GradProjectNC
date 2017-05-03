package com.grad.project.nc.controller.api.admin;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/products")
@Slf4j
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


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    private Map<String, String> update(@RequestBody FrontendProduct product) {
        Map<String, String> response = new HashMap<>();

        //TODO parse, validate and write to database
        log.info(product.toString());

        response.put("status", "success");//or error
        response.put("message", product.getId() < 0 ? "Product successfully added" : "Product successfully updated");
        response.put("id", product.getId().toString());//put here id of added object
        return response;
    }

    @Data
    @NoArgsConstructor
    private static class FrontendProduct {
        private Long id; //if id < 0 - it is a new product, otherwise - edited
        private Long productTypeId;
        private String name;
        private String description;
        private boolean isActive;
        private Map<Long, Integer> prices;  //region id - price
        private Map<Long, String> characteristicValues;  //characteristic id - value (dates are coming in such format: 2017-05-01T23:03:57)
    }

}
