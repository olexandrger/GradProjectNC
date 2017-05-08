package com.grad.project.nc.persistence;

import com.grad.project.nc.model.ProductCharacteristicValue;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductCharacteristicValueDao {

    ProductCharacteristicValue add(ProductCharacteristicValue value);

    ProductCharacteristicValue update(ProductCharacteristicValue value);

    ProductCharacteristicValue find(Long productId, Long productCharacteristicId);

    List<ProductCharacteristicValue> findAll();

    void delete(Long productId, Long productCharacteristicId);

    List<ProductCharacteristicValue> findByProductId(Long productId);

    void deleteByProductId(Long productId);

    @Transactional
    void persistBatch(Long productId, List<ProductCharacteristicValue> values);
}
