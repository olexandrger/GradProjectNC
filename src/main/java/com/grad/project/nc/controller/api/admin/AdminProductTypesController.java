package com.grad.project.nc.controller.api.admin;

import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.ProductCharacteristic;
import com.grad.project.nc.model.ProductType;
import com.grad.project.nc.persistence.CategoryDao;
import com.grad.project.nc.persistence.impl.ProductCharacteristicDaoImpl;
import com.grad.project.nc.persistence.impl.ProductTypeDaoImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminProductTypesController {

    private ProductTypeDaoImpl productTypeDao;
    private CategoryDao categoryDao;
    private ProductCharacteristicDaoImpl productCharacteristicDao;

    @Autowired
    public AdminProductTypesController(ProductTypeDaoImpl productTypeDao, CategoryDao categoryDao, ProductCharacteristicDaoImpl productCharacteristicDao) {
        this.productTypeDao = productTypeDao;
        this.categoryDao = categoryDao;
        this.productCharacteristicDao = productCharacteristicDao;
    }

    @RequestMapping(value = "/dataTypes", method = RequestMethod.GET)
    public Map<Long, String> dataTypes() {
        Map<Long, String> result = new HashMap<>();
        //dataTypeDao.findAll().forEach(value -> result.put(value.getDataTypeId(), value.getDataType()));
        return result;
    }

    @RequestMapping(value = "productTypes/delete", method = RequestMethod.POST)
    public Map<String, String> deleteType(@RequestBody Map<String, Long> productTypeId) {
        Map<String, String> result = new HashMap<>();
        productTypeDao.delete(productTypeId.get("id"));
        result.put("status", "success");
        result.put("message", "Product deleted successfully");
        return result;
    }

    @RequestMapping(value = "/productTypes/update", method = RequestMethod.POST)
    public Map<String, String> updateType(@RequestBody Type type) {
        Map<String, String> result = new HashMap<>();
        //TODO deal with edited types
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
                Category dataType = categoryDao.find(c.getDataTypeId());
                if (dataType == null) {
                    result.put("status", "error");
                    result.put("message", "Invalid data type in characteristic " + c.getName());
                    return result;
                }

                ProductCharacteristic characteristic = new ProductCharacteristic();
                characteristic.setCharacteristicName(c.getName());
                characteristic.setDataType(dataType);
                characteristic.setMeasure(c.getMeasure());
                characteristic.setProductType(productType);
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
            log.error(exception.toString());
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
