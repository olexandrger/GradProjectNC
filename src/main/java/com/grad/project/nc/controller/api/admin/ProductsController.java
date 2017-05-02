package com.grad.project.nc.controller.api.admin;

import com.grad.project.nc.model.DataType;
import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductCharacteristic;
import com.grad.project.nc.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/products")
@Slf4j
public class ProductsController {

    private ProductDao productDao;
    private ProductCharacteristicValueDao productCharacteristicValueDao;
    private ProductCharacteristicDao productCharacteristicDao;
    private DataTypeDao dataTypeDao;
    private ProductRegionPriceDao productRegionPriceDao;

    @Autowired
    public ProductsController(ProductDao productDao, ProductCharacteristicValueDao productCharacteristicValueDao,
                              ProductCharacteristicDao productCharacteristicDao, DataTypeDao dataTypeDao,
                              ProductRegionPriceDao productRegionPriceDao) {
        this.productDao = productDao;
        this.productCharacteristicValueDao = productCharacteristicValueDao;
        this.productCharacteristicDao = productCharacteristicDao;
        this.dataTypeDao = dataTypeDao;
        this.productRegionPriceDao = productRegionPriceDao;
    }

    @RequestMapping("/all")
    private Collection<Product> getAll() {
        Collection<Product> products = productDao.findAll();

//        products.forEach((item)-> {log.error(item.toString());});
        products.forEach((item) -> {
            item.getProductCharacteristics().forEach((item2) -> {
                if (item2.getDataType() == null)
                    log.error("data type is null");
                else log.error(item2.getDataType().getDataType());
            });
        });
        log.info("RETURN");
        return products;
    }
/*
    @RequestMapping("/all")
    private List<Product> getAll() {

        return productDao.findAll().stream().map((item) -> {
            log.error(item.getClass().toString());

            Product product = new Product();

            product.setId(item.getProductId());
            product.setName(item.getName());
            product.setDescription(item.getDescription());
            product.setProductTypeId(item.getProductType().getProductTypeId());
            product.setActive(item.getIsActive());


            product.setCharacteristics(item.getProductCharacteristicValues().stream().map((c) -> {
                CharacteristicValue value = new CharacteristicValue();
                value.setCharacteristicId(c.getProductCharacteristicId());

                //TODO rework after product characteristic value dao finished
                DataType dataType = dataTypeDao.find(
                        productCharacteristicDao.find(c.getProductCharacteristicId())
                                .getDataTypeId());

                if ("NUMBER".equals(dataType.getDataType())) {
                    value.setValue(c.getNumberValue().toString());
                } else if ("DATE".equals(dataType.getDataType())) {
                    //maybe rework it
                    value.setValue(Long.toString(c.getDateValue().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
                } else if ("STRING".equals(dataType.getDataType())) {
                    value.setValue(c.getStringValue());
                } else {
                    throw new RuntimeException("Can not recognise data type: " + dataType.getDataType());
                }

                return value;
            }).collect(Collectors.toList()));

            product.setPrices(productRegionPriceDao.getPricesByProduct(item).stream()
                    .map((p) -> new Price(p.getRegion().getRegionName(), p.getPrice()))
                    .collect(Collectors.toList()));

            return product;
        }).collect(Collectors.toList());
    }
*//*
    @Data
    @NoArgsConstructor
    private class Product {
        private Long id;
        private String name;
        private String description;
        private Long productTypeId;
        private boolean active;
        List<CharacteristicValue> characteristics;
        List<Price> prices;
    }

    @Data
    @NoArgsConstructor
    private class CharacteristicValue {
        private Long characteristicId;
        private String value;
    }*/

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class Price {
        private String region;
        private double price;
    }
}
