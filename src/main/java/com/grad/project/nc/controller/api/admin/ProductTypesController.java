package com.grad.project.nc.controller.api.admin;

import com.grad.project.nc.model.DataType;
import com.grad.project.nc.model.ProductCharacteristic;
import com.grad.project.nc.model.ProductType;
import com.grad.project.nc.persistence.DataTypeDao;
import com.grad.project.nc.persistence.ProductCharacteristicDao;
import com.grad.project.nc.persistence.ProductTypeDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class ProductTypesController {

    private ProductTypeDao productTypeDao;
    private DataTypeDao dataTypeDao;
    private ProductCharacteristicDao productCharacteristicDao;

    @Autowired
    public ProductTypesController(ProductTypeDao productTypeDao, DataTypeDao dataTypeDao, ProductCharacteristicDao productCharacteristicDao) {
        this.productTypeDao = productTypeDao;
        this.dataTypeDao = dataTypeDao;
        this.productCharacteristicDao = productCharacteristicDao;
    }

    @RequestMapping(value = "/dataTypes", method = RequestMethod.GET)
    public Map<Long, String> dataTypes() {
        Map<Long, String> result = new HashMap<>();
        dataTypeDao.findAll().forEach(value -> result.put(value.getDataTypeId(), value.getDataType()));
        return result;
    }

    @RequestMapping(value = "/productTypes/get/{id}", method = RequestMethod.GET)
    public Type getById(@PathVariable("id") Long id) {
        ProductType pt = productTypeDao.find(id);
        Type type = new Type();
        type.setId(pt.getProductTypeId());
        type.setName(pt.getProductTypeName());
        type.setDescription(pt.getProductTypeDescription());
        type.setCharacteristics(productCharacteristicDao.findByProductId(type.getId()).stream()
                .map(item -> new Characteristic(item.getProductCharacteristicId(), item.getCharacteristicName(),
                        item.getMeasure(), item.getDataType().getDataTypeId()))
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
            type.setCharacteristics(productCharacteristicDao.findByProductId(type.getId()).stream()
                    .map(item -> new Characteristic(item.getProductCharacteristicId(), item.getCharacteristicName(),
                                                        item.getMeasure(), item.getDataType().getDataTypeId()))
                    .collect(Collectors.toList()));
            return type;
        }).collect(Collectors.toList());
    }

    @RequestMapping(value = "productTypes/delete", method = RequestMethod.POST)
    public Map<String, String> deleteType(@RequestBody Map<String, Integer> productTypeId) {
        Map<String, String> result = new HashMap<>();
        productTypeDao.delete(productTypeDao.find(productTypeId.get("id")));
        result.put("status", "success");
        result.put("message", "Product deleted successfully");
        return result;
    }

    @RequestMapping(value = "/productTypes/update", method = RequestMethod.POST)
    public Map<String, String> updateType(@RequestBody Type type) {
        Map<String, String> result = new HashMap<>();

        try {
            ProductType productType = new ProductType();
            productType.setProductTypeId(type.getId());
            productType.setProductTypeName(type.getName());
            productType.setProductTypeDescription(type.getDescription());

            if (productTypeDao.find(type.getId()) != null) {
                productType = productTypeDao.update(productType);
                result.put("message", "Product successfully updated");
            } else {
                productType = productTypeDao.add(productType);
                result.put("message", "Product successfully added");
            }

            productType.setProductCharacteristics(new LinkedList<>());

            for (Characteristic c : type.getCharacteristics()) {
                DataType dataType = dataTypeDao.find(c.getDataTypeId());
                if (dataType == null) {
                    result.put("status", "error");
                    result.put("message", "Invalid data type in characteristic " + c.getName());
                    return result;
                }

                ProductCharacteristic characteristic = new ProductCharacteristic();
                characteristic.setCharacteristicName(c.getName());
                characteristic.setDataType(dataType);
                characteristic.setMeasure(c.getMeasure());
                characteristic.setProductTypeId(productType.getProductTypeId());
                if (c.getId() < 0) {
                    productCharacteristicDao.add(characteristic);
                } else {
                    characteristic.setProductCharacteristicId(c.getId());
                    productCharacteristicDao.update(characteristic);
                }
                productType.getProductCharacteristics().add(characteristic);
            }
            productTypeDao.update(productType);

            result.put("status", "success");
            result.put("id", productType.getProductTypeId().toString());
        } catch (DataAccessException exception) {
            result.put("status", "error");
            result.put("message", "Can not add info to database");

            return result;
        }

        return result;
    }

    @Data
    @NoArgsConstructor
    private static class Type {
        private Long id;
        private String name;
        private String description;
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
