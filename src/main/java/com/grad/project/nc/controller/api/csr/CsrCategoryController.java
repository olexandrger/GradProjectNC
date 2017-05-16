package com.grad.project.nc.controller.api.csr;

import com.grad.project.nc.controller.api.dto.FrontendCategory;
import com.grad.project.nc.model.ProductOrder;
import com.grad.project.nc.service.Category.CategoryService;
import com.grad.project.nc.service.instances.InstanceService;
import com.grad.project.nc.service.orders.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by DeniG on 11.05.2017.
 */
@RestController
@RequestMapping("/api/csr/category")
public class CsrCategoryController {

    CategoryService categoryService;
    InstanceService instanceService;
    OrdersService ordersService;

    @Autowired
    public CsrCategoryController(CategoryService categoryService, InstanceService instanceService, OrdersService ordersService) {
        this.categoryService = categoryService;
        this.instanceService = instanceService;
        this.ordersService = ordersService;
    }

    @RequestMapping(value = "/getstatus/frominstance/{id}", method = RequestMethod.GET)
    public Map<String, Object> getCategoryByInstanceId(@PathVariable Long id) {

        Map<String, Object> result = new HashMap<>();
        Collection<ProductOrder> openOrders = ordersService.getOpenInstanceOrders(id, 2, 0);
        if(openOrders.size()==0){
            result.put("openOrders", "false" );
            result.put("productStatus", FrontendCategory.fromEntity(categoryService.getByProductInstanceId(id)));
        } else {
            result.put("openOrders", "true" );
        }
        return result;
    }

}
