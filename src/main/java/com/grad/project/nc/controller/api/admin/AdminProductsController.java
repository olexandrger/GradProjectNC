package com.grad.project.nc.controller.api.admin;

import com.grad.project.nc.model.*;
import com.grad.project.nc.service.product.ProductService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/products")
@Slf4j
public class AdminProductsController {

    private ProductService productService;

    @Autowired
    public AdminProductsController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, String> update(@RequestBody FrontendProduct frontendProduct) {
        Map<String, String> response = new HashMap<>();

        //TODO parse, validate and write to database + split
        log.info("UPDATING " + frontendProduct.toString());

        Product p = extractProduct(frontendProduct);

        productService.update(p);

        response.put("status", "success");//or error
        response.put("message", "Product successfully updated");
        response.put("id", p.getProductId().toString());//put here id of added object
        return response;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Map<String, String> add(@RequestBody FrontendProduct frontendProduct) {
        Map<String, String> response = new HashMap<>();
        //TODO parse, validate and write to database + split
        log.info(String.valueOf(frontendProduct.getIsActive()));
        log.info("ADDING " + frontendProduct.toString());

        Product p = extractProduct(frontendProduct);

        productService.add(p);

        response.put("status", "success");//or error
        response.put("message", "Product successfully added");
        response.put("id", frontendProduct.getId().toString());//put here id of added object
        return response;
    }

    private Product extractProduct(FrontendProduct frontendProduct) {
        Product p = new Product();
        p.setProductId(frontendProduct.getId());
        p.setProductName(frontendProduct.getName());
        p.setProductDescription(frontendProduct.getDescription());
        p.setIsActive(frontendProduct.getIsActive());
        p.setProductType(new ProductType(frontendProduct.getProductTypeId()));
        p.setPrices(frontendProduct.getPrices().entrySet()
                .stream()
                .map((Map.Entry<Long, Double> entry) -> new ProductRegionPrice(new Region(entry.getKey()),
                        entry.getValue()))
                .collect(Collectors.toList()));
        p.setProductCharacteristicValues(frontendProduct.getCharacteristicValues().entrySet()
                .stream()
                .map((Map.Entry<Long, String> entry) -> new ProductCharacteristicValue
                        ( new ProductCharacteristic(entry.getKey()), entry.getValue()))
                .collect(Collectors.toList()));

        return p;
    }

    @Data
    @NoArgsConstructor
    private static class FrontendProduct {
        private Long id; //if id < 0 - it is a new product, otherwise - edited
        private Long productTypeId;
        private String name;
        private String description;
        private Boolean isActive;
        private Map<Long, Double> prices;  //region id - price
        private Map<Long, String> characteristicValues;  //characteristic id - value (dates are coming in such format: 2017-05-01T23:03:57)
    }
}
