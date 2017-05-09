package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}