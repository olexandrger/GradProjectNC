package com.grad.project.nc.service.reports;


import com.grad.project.nc.model.Report;
import com.grad.project.nc.persistence.ReportDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
        GeneratedReport report = generateReport(id, parameters);
        XlsWorkbook workbook = new XlsWorkbook();
        workbook.setCurrentSheet("report");

        for (int i = 0; i < report.getHeader().size(); i++) {
            workbook.writeCell(0, i, report.getHeader().get(i));
        }

        for (int i = 0; i < report.getData().size(); i++) {
            for (int j = 0; j < report.getData().get(i).size(); j++) {
                workbook.writeCell(i + 1, j, report.getData().get(i).get(j));
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
