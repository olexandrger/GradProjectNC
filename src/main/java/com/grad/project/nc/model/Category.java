package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Category {
    private Long categoryId;
    private String categoryName;
    private CategoryType categoryType;
}
