package com.grad.project.nc.service.reports;

import com.grad.project.nc.model.Report;

import java.util.List;
import java.util.Map;

public interface ReportService {
    List<Report> getReports();
    GeneratedReport generateReport(Long id, Map<Integer, String> parameters);
}
