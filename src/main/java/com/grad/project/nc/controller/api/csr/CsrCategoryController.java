package com.grad.project.nc.controller.api.csr;

import com.grad.project.nc.controller.api.dto.FrontendCategory;
import com.grad.project.nc.service.Category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by DeniG on 11.05.2017.
 */
@RestController
@RequestMapping("/api/csr/category")
public class CsrCategoryController {

    CategoryService categoryService;

    @Autowired
    public CsrCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/getstatus/frominstance/{id}", method = RequestMethod.GET)
    public FrontendCategory getCategoryByInstanceId(@PathVariable Long id){
        return FrontendCategory.fromEntity(categoryService.getByProductInstanceId(id));
    }

}
