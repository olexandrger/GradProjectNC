package com.grad.project.nc.controller.api.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class ResponseHolder {
    private String status;
    private Map<String, Object> values = new HashMap<>();
}
