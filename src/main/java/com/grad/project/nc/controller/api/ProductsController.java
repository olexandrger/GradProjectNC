package com.grad.project.nc.controller.api;

import com.grad.project.nc.model.*;
import com.grad.project.nc.persistence.ProductDao;
import com.grad.project.nc.persistence.RegionDao;
import com.grad.project.nc.service.product.ProductService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/products")
public class ProductsController {

    private ProductDao productDao;
    private ProductService productService;

    //TODO maybe extract it
    private static FrontendCategory createFrontendCategory(Category category) {
        return FrontendCategory.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }

    private static FrontendCharacteristicValue createFrontendCharacteristicValue(ProductCharacteristicValue value) {
        return FrontendCharacteristicValue.builder()
                .productCharacteristic(createFrontendCharacteristic(value.getProductCharacteristic()))
                .dateValue(value.getDateValue())
                .stringValue(value.getStringValue())
                .numberValue(value.getNumberValue())
                .build();
    }

    private static FrontendCharacteristic createFrontendCharacteristic(ProductCharacteristic characteristic) {
        return FrontendCharacteristic.builder()
                .productCharacteristicId(characteristic.getProductCharacteristicId())
                .characteristicName(characteristic.getCharacteristicName())
                .measure(characteristic.getMeasure())
                .dataType(createFrontendCategory(characteristic.getDataType()))
                .build();
    }

    private static FrontendProductType createFrontendProductType(ProductType productType) {
        return FrontendProductType.builder()
                .productTypeId(productType.getProductTypeId())
                .productTypeName(productType.getProductTypeName())
                .productTypeDescription(productType.getProductTypeDescription())
                .isActive(productType.getIsActive())
                .build();
    }

    private static FrontendRegion createFrontendRegion(Region region) {
        return FrontendRegion.builder()
                .regionId(region.getRegionId())
                .regionName(region.getRegionName())
                .build();
    }

    private static FrontendPrice createFrontendPrice(ProductRegionPrice price) {
        return FrontendPrice.builder()
                .priceId(price.getPriceId())
                .price(price.getPrice())
                .region(createFrontendRegion((price.getRegion())))
                .build();
    }

    private static FrontendProduct createFrontendProduct(Product product) {
        return FrontendProduct.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .isActive(product.getIsActive())
                .productType(createFrontendProductType(product.getProductType()))
//                .productCharacteristics(product.getProductCharacteristics().stream().map(ProductsController::createFrontendCharacteristic).collect(Collectors.toList()))
                .productCharacteristicValues(product.getProductCharacteristicValues().stream().map(ProductsController::createFrontendCharacteristicValue).collect(Collectors.toList()))
                .prices(product.getPrices().stream().map(ProductsController::createFrontendPrice).collect(Collectors.toList()))
                .build();

    }

    @Autowired
    public ProductsController(ProductDao productDao, ProductService productService) {
        this.productDao = productDao;
        this.productService = productService;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public FrontendProduct getById(@PathVariable("id") Long id) {
        return createFrontendProduct(productService.find(id));
    }

    @RequestMapping(value = "/byRegion/{id}", method = RequestMethod.GET)
    public Collection<FrontendProduct> getByRegion(@PathVariable("id") Long id) {
        return productDao.findByRegionId(id).stream().filter(Product::getIsActive).map(ProductsController::createFrontendProduct).collect(Collectors.toList());
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Collection<FrontendProduct> getAll() {
        return productDao.findAll().stream().map(ProductsController::createFrontendProduct).collect(Collectors.toList());
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class FrontendProductType {
        private Long productTypeId;
        private String productTypeName;
        private String productTypeDescription;
        private Boolean isActive;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class FrontendCategory {
        private Long categoryId;
        private String categoryName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class FrontendCharacteristic {
        private Long productCharacteristicId;
        private String characteristicName;
        private String measure;
        private FrontendCategory dataType;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class FrontendCharacteristicValue {
        private FrontendCharacteristic productCharacteristic;
        private Number numberValue;
        private OffsetDateTime dateValue;
        private String stringValue;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class FrontendRegion {
        private Long regionId;
        private String regionName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class FrontendPrice {
        private Long priceId;
        private double price;
        private FrontendRegion region;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class FrontendProduct {
        private Long productId;
        private String productName;
        private String productDescription;
        private Boolean isActive;
        private FrontendProductType productType;

        private List<FrontendCharacteristicValue> productCharacteristicValues;
//        private List<FrontendCharacteristic> productCharacteristics;
        private List<FrontendPrice> prices;
    }
}
