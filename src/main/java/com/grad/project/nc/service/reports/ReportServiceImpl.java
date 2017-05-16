package com.grad.project.nc.service.reports;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grad.project.nc.model.Report;
import com.grad.project.nc.persistence.ReportDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    private JdbcTemplate jdbcTemplate;
    private ReportDao reportDao;

    @Autowired
    public ReportServiceImpl(JdbcTemplate jdbcTemplate, ReportDao reportDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.reportDao = reportDao;
    }

    @Override
    public List<Report> getReports() {
        //TODO filter
        return reportDao.findAll();
    }

    @Override
    public GeneratedReport generateReport(Long id, Map<Integer, String> parameters) {
        //TODO check authorities
        Report report = reportDao.find(id);

        String query = report.getReportScript();

        Object[] params = new String[parameters.size()];
        parameters.forEach((key, value) -> params[key] = value);

        return jdbcTemplate.query(query, new ReportExtractor(), params);
    }

    @Override
    public XlsWorkbook generateXlsReport(Long id, Map<Integer, String> parameters) {
        GeneratedReport generatedReport = generateReport(id, parameters);
        XlsWorkbook workbook = new XlsWorkbook();
        workbook.setCurrentSheet("report");

        Report report = reportDao.find(id);

        int heightOffset = 0;

        workbook.writeCell(heightOffset, 0, "Report: ");
        workbook.writeCell(heightOffset, 1, report.getReportName());
        heightOffset++;

        if (parameters.size() > 0) {
            try {
                workbook.writeCell(heightOffset, 0, "Parameters:");
                heightOffset++;

                ObjectMapper mapper = new ObjectMapper();
                TypeReference<List<HashMap<String, String>>> ref = new TypeReference<List<HashMap<String, String>>>() {
                };

                List<Map<String, String>> paramsDescription = mapper.readValue(report.getParameters(), ref);

                for (Map<String, String> param : paramsDescription) {
                    int paramId = Integer.parseInt(param.get("id"));
                    String paramName = param.get("name");
                    String paramValue = parameters.get(paramId);
                    workbook.writeCell(heightOffset, 0, paramName);
                    workbook.writeCell(heightOffset, 1, paramValue);
                    heightOffset++;
                }

            } catch (IOException | NumberFormatException e) {
                log.error("Can not write parameters: ", e);
            }
        }
        heightOffset++;

        for (int i = 0; i < generatedReport.getHeader().size(); i++) {
            workbook.writeCell(heightOffset, i, generatedReport.getHeader().get(i));
        }
        heightOffset++;

        for (int i = 0; i < generatedReport.getData().size(); i++) {
            for (int j = 0; j < generatedReport.getData().get(i).size(); j++) {
                workbook.writeCell(i + heightOffset, j, generatedReport.getData().get(i).get(j));
            }
        }

        return workbook;
    }

    private static class ReportExtractor implements ResultSetExtractor<GeneratedReport> {

        @Override
        public GeneratedReport extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            ResultSetMetaData metaData = resultSet.getMetaData();

            List<String> header = new LinkedList<>();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                header.add(metaData.getColumnName(i));
            }

            GeneratedReport report = new GeneratedReport();
            report.setHeader(header);
            report.setData(new LinkedList<>());

            while (resultSet.next()) {
                List<String> row = new LinkedList<>();
                for (int i = 1; i <= columnCount; i++) {
                    Object value = resultSet.getObject(i);
                    if (value == null) {
                        row.add("");
                    } else {
                        row.add(value.toString());
                    }
                }
                report.getData().add(row);
            }

            return report;
        }
    }

}
