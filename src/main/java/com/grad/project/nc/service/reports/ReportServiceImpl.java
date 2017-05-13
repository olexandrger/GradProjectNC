package com.grad.project.nc.service.reports;


import com.grad.project.nc.model.Report;
import com.grad.project.nc.persistence.ReportDao;
import com.grad.project.nc.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

        return null;
    }


}
