package com.grad.project.nc.controller.api.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grad.project.nc.model.Report;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@Slf4j
public class FrontendReportInfo {
    private Long id;
    private String name;
    private String description;
    private List<Param> params;

    public static FrontendReportInfo fromEntity(Report report) {
        return FrontendReportInfo.builder()
                .id(report.getReportId())
                .name(report.getReportName())
                .description(report.getReportDescription())
                .params(Param.getFromReport(report.getParameters()))
                .build();
    }

    @Data
    private static class Param {
        private Long id;
        private String name;
        private String dataType;

        public static List<Param> getFromReport(String reportParams) {
            List<Param> params = new LinkedList<>();

            try {
                ObjectMapper mapper = new ObjectMapper();
                params = mapper.readerFor(params.getClass()).readValue(reportParams);
            } catch (IOException e) {
                log.error("Can not parse report parameters", e);
            }

            return params;
        }
    }
}
