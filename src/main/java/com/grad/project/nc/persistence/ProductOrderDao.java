package com.grad.project.nc.persistence;

import com.grad.project.nc.model.ProductOrder;

import java.util.List;

public interface ProductOrderDao extends CrudDao<ProductOrder> {

    List<ProductOrder> findByUserId(Long id);

    List<ProductOrder> findByProductName(String productName);

    List<ProductOrder> findByUserLastName(String lastName);

    List<ProductOrder> findByUserPhoneNumber(String phoneNumber);

    List<ProductOrder> findByProductInstanceId(Long id);
}
