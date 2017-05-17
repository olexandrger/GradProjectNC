package com.grad.project.nc.controller.api.pmg;

import com.grad.project.nc.model.Category;
import com.grad.project.nc.persistence.CategoryDao;
import com.grad.project.nc.service.Category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DeniG on 17.05.2017.
 */
@RestController
@RequestMapping("api/pmg/category")
public class PmgCategoryController {
    CategoryService categoryService;

    @Autowired
    public PmgCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @RequestMapping(value = "/get/bytype/{typename}/", method = RequestMethod.GET)
    public Map<String, Object> findByCategoryTypeName(@PathVariable String typename){
        Map<String, Object> result = new HashMap<>();
        Collection<Category> categories = categoryService.findByCategoryTypeName(typename);
        if(categories.size()>0){
            result.put("status", "found");
            result.put("categories", categories);
        } else {
            result.put("status", "not found");
        }
        return result;
    }
}
