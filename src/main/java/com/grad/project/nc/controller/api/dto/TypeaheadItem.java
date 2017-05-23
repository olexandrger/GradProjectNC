package com.grad.project.nc.controller.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class TypeaheadItem {
    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String name;
}
