package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FrontendCategory {
    private Long categoryId;
    private String categoryName;

    public static FrontendCategory fromEntity(Category category) {
        return FrontendCategory.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }

    public Category toModel() {
        return Category.builder()
                .categoryId(getCategoryId())
                .categoryName(getCategoryName())
                .build();
    }
}