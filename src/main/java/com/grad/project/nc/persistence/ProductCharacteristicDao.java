package com.grad.project.nc.persistence;

import com.grad.project.nc.model.ProductCharacteristic;

import java.util.List;

public interface ProductCharacteristicDao extends CrudDao<ProductCharacteristic> {

    List<ProductCharacteristic> findByProductTypeId(Long productTypeId);

    List<ProductCharacteristic> findByDataTypeId(Long dataTypeId);

    void updateBatch(List<ProductCharacteristic> productCharacteristics);

    void deleteBatch(List<ProductCharacteristic> productCharacteristics);

    void persistBatch(List<ProductCharacteristic> productCharacteristics);

    void deleteByProductTypeId(Long productTypeId);
}
