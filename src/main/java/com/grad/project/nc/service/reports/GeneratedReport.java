package com.grad.project.nc.service.reports;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class GeneratedReport {
    private List<String> header;
    private List<List<String>> data;
}
