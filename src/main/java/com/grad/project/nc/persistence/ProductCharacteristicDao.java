package com.grad.project.nc.persistence;

import com.grad.project.nc.model.ProductCharacteristic;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductCharacteristicDao extends CrudDao<ProductCharacteristic> {

    List<ProductCharacteristic> findByProductTypeId(Long productTypeId);

    List<ProductCharacteristic> findByDataTypeId(Long dataTypeId);

    @Transactional
    void persistProductTypeProductCharacteristics(Long productTypeId,
                                                  List<ProductCharacteristic> productCharacteristics);

    void deleteByProductTypeId(Long productTypeId);
}
