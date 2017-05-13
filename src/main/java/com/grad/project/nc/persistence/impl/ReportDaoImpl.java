package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.Report;
import com.grad.project.nc.model.Role;
import com.grad.project.nc.persistence.AbstractDao;
import com.grad.project.nc.persistence.ReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ReportDaoImpl extends AbstractDao implements ReportDao {

    private static final String PK_COLUMN_NAME = "report_id";

    @Autowired
    public ReportDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Report add(Report report) {
        String insertQuery = "INSERT INTO report(report_name, report_description, report_script, parameters) VALUES (?, ?, ?, ?)";

        Long id = executeInsertWithId(insertQuery, PK_COLUMN_NAME, report.getReportName(),
                report.getReportDescription(), report.getReportScript(), report.getParameters());

        report.setReportId(id);

        return report;
    }

    @Override
    public Report update(Report report) {
        String query = "UPDATE report SET report_name=?, report_description=?, report_script=?, parameters=? WHERE report_id = ?";

        executeUpdate(query, report.getReportName(), report.getReportDescription(), report.getReportScript(),
                report.getParameters(), report.getReportId());

        return report;
    }

    @Override
    public Report find(Long id) {
        String findOneQuery = "SELECT report_id, report_name, report_description, report_script, parameters FROM report WHERE report_id=?";

        return findOne(findOneQuery, new ReportMapper(), id);
    }

    @Override
    public List<Report> findAll() {
        String findAllQuery = "SELECT report_id, report_name, report_description, report_script, parameters FROM report";

        return findMultiple(findAllQuery, new ReportMapper());
    }

    @Override
    public void delete(Long id) {
        String deleteQuery = "DELETE FROM \"report\" WHERE \"report_id\" = ?";
        executeUpdate(deleteQuery, id);
    }

    private class ReportMapper implements RowMapper<Report> {

        @Override
        public Report mapRow(ResultSet rs, int rowNum) throws SQLException {
            Report report = new Report();

            report.setReportId(rs.getLong("report_id"));
            report.setReportName(rs.getString("report_name"));
            report.setReportDescription(rs.getString("report_description"));
            report.setReportScript(rs.getString("report_script"));
            report.setParameters(rs.getString("parameters"));

            return report;
        }
    }
}
