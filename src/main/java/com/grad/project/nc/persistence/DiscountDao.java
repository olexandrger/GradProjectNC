package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Discount;

import java.util.List;

public interface DiscountDao extends CrudDao<Discount> {

    List<Discount> findByProductRegionPriceId(Long productRegionPriceId);
}
