package com.grad.project.nc.persistence;

import com.grad.project.nc.model.ProductCharacteristicValue;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductCharacteristicValueDao {

    ProductCharacteristicValue find(Long productId, Long productCharacteristicId);

    List<ProductCharacteristicValue> findByProductId(Long id);

    void deleteProductCharacteristicValuesByProductId(Long productId);

    @Transactional
    void addBatch(List<ProductCharacteristicValue> values);
}
