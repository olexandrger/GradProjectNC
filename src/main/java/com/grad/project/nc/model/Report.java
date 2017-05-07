package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Report {
    private Long reportId;
    private String reportName;
    private String reportDescription;
    private String reportScript;
    private String parameters;
}