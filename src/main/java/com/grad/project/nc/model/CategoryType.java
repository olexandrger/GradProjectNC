package com.grad.project.nc.model;

/**
 * Created by Alex on 4/24/2017.
 */
public class CategoryType {
    private Long categoryTypeId;
    private String categoryTypeName;

    public Long getCategoryTypeId() {
        return categoryTypeId;
    }

    public void setCategoryTypeId(Long categoryTypeId) {
        this.categoryTypeId = categoryTypeId;
    }

    public String getCategoryTypeName() {
        return categoryTypeName;
    }

    public void setCategoryTypeName(String categoryTypeName) {
        this.categoryTypeName = categoryTypeName;
    }
}
