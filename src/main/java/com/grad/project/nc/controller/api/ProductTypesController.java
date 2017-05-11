package com.grad.project.nc.controller.api;

import com.grad.project.nc.model.ProductType;
import com.grad.project.nc.persistence.CategoryDao;
import com.grad.project.nc.persistence.ProductCharacteristicDao;
import com.grad.project.nc.persistence.ProductTypeDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class ProductTypesController {

    private ProductTypeDao productTypeDao;
    private ProductCharacteristicDao productCharacteristicDao;
    private final CategoryDao categoryDao;

    @Autowired
    public ProductTypesController(ProductTypeDao productTypeDao, ProductCharacteristicDao productCharacteristicDao,
                                  CategoryDao categoryDao) {
        this.productTypeDao = productTypeDao;
        this.productCharacteristicDao = productCharacteristicDao;
        this.categoryDao = categoryDao;
    }

    @RequestMapping(value = "/dataTypes", method = RequestMethod.GET)
    public Map<Long, String> dataTypes() {
        Map<Long, String> result = new HashMap<>();
        categoryDao.findByCategoryTypeName("DATA_TYPE")
                .forEach(value -> result.put(value.getCategoryId(), value.getCategoryName()));
        return result;
    }

    @RequestMapping(value = "/productTypes/get/{id}", method = RequestMethod.GET)
    public Type getById(@PathVariable("id") Long id) {
        ProductType pt = productTypeDao.find(id);
        Type type = new Type();
        type.setId(pt.getProductTypeId());
        type.setName(pt.getProductTypeName());
        type.setDescription(pt.getProductTypeDescription());
        type.setActive(pt.getIsActive());
        type.setCharacteristics(productCharacteristicDao.findByProductTypeId(type.getId()).stream()
                .map(item -> new Characteristic(item.getProductCharacteristicId(), item.getCharacteristicName(),
                        item.getMeasure(), item.getDataType().getCategoryId()))
                .collect(Collectors.toList()));
        return type;
    }

    @RequestMapping(value = "/productTypes/all", method = RequestMethod.GET)
    public List<Type> getAll() {
        return productTypeDao.findAll().stream().map(productType -> {
            Type type = new Type();
            type.setId(productType.getProductTypeId());
            type.setName(productType.getProductTypeName());
            type.setDescription(productType.getProductTypeDescription());
            type.setActive(productType.getIsActive());
            type.setCharacteristics(productCharacteristicDao.findByProductTypeId(type.getId()).stream()
                    .map(item -> new Characteristic(item.getProductCharacteristicId(), item.getCharacteristicName(),
                                                        item.getMeasure(), item.getDataType().getCategoryId()))
                    .collect(Collectors.toList()));
            return type;
        }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/productTypes/first", method = RequestMethod.GET)
    public List<Type> getFirstN() {
        return productTypeDao.findFirstN(5).stream().map(productType -> {
            Type type = new Type();
            type.setId(productType.getProductTypeId());
            type.setName(productType.getProductTypeName());
            type.setDescription(productType.getProductTypeDescription());
            type.setActive(productType.getIsActive());
            type.setCharacteristics(productCharacteristicDao.findByProductTypeId(type.getId()).stream()
                    .map(item -> new Characteristic(item.getProductCharacteristicId(), item.getCharacteristicName(),
                            item.getMeasure(), item.getDataType().getCategoryId()))
                    .collect(Collectors.toList()));
            return type;
        }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/productTypes/last", method = RequestMethod.GET)
    public List<Type> getLastN() {
        return productTypeDao.findLastN(5).stream().map(productType -> {
            Type type = new Type();
            type.setId(productType.getProductTypeId());
            type.setName(productType.getProductTypeName());
            type.setDescription(productType.getProductTypeDescription());
            type.setActive(productType.getIsActive());
            type.setCharacteristics(productCharacteristicDao.findByProductTypeId(type.getId()).stream()
                    .map(item -> new Characteristic(item.getProductCharacteristicId(), item.getCharacteristicName(),
                            item.getMeasure(), item.getDataType().getCategoryId()))
                    .collect(Collectors.toList()));
            return type;
        }).collect(Collectors.toList());
    }

    @Data
    @NoArgsConstructor
    private static class Type {
        private Long id;
        private String name;
        private String description;
        private Boolean active;
        private Collection<Characteristic> characteristics;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Characteristic {
        private Long id;
        private String name;
        private String measure;
        private Long dataTypeId;
    }
}
