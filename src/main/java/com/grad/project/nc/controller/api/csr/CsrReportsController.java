package com.grad.project.nc.controller.api.csr;

import com.grad.project.nc.controller.api.dto.FrontendReportInfo;
import com.grad.project.nc.service.reports.GeneratedReport;
import com.grad.project.nc.service.reports.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/csr/reports")
public class CsrReportsController {

    private ReportService reportService;

    @Autowired
    public CsrReportsController(ReportService reportService) {
        this.reportService = reportService;
    }

    @RequestMapping(value = "/get/all", method = RequestMethod.GET)
    public List<FrontendReportInfo> getAll() {
        return reportService.getReports().stream().map(FrontendReportInfo::fromEntity).collect(Collectors.toList());
    }

    @RequestMapping(value = "/generate/{id}", method = RequestMethod.POST)
    public GeneratedReport generateReport(@PathVariable Long id, @RequestBody Map<Integer, String> parameters) {
        return reportService.generateReport(id, parameters);
    }
}
