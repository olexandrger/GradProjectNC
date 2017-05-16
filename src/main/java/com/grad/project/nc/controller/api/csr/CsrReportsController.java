package com.grad.project.nc.controller.api.csr;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grad.project.nc.controller.api.dto.FrontendReportInfo;
import com.grad.project.nc.service.reports.GeneratedReport;
import com.grad.project.nc.service.reports.ReportService;
import com.grad.project.nc.service.reports.XlsWorkbook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/csr/reports")
@Slf4j
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

    @RequestMapping(value = "/generate/{id}/xls/{encodedParams}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> generateToXls(@PathVariable Long id, @PathVariable String encodedParams) throws IOException {
        byte[] data = DatatypeConverter.parseBase64Binary(encodedParams);
        TypeReference<HashMap<Integer,String>> typeRef = new TypeReference<HashMap<Integer,String>>() {};
        Map<Integer, String> parameters = new ObjectMapper().readValue(data, typeRef);

        XlsWorkbook report = reportService.generateXlsReport(id, parameters);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        report.writeToOutputStream(bytes);

        String filename = "report-" + System.currentTimeMillis() + ".xls";

        return ResponseEntity
                .ok()
                .contentLength(bytes.size())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header("content-disposition", "attachment; filename=\"" + filename +"\"")
                .body(new InputStreamResource(new ByteArrayInputStream(bytes.toByteArray())));
    }
}
