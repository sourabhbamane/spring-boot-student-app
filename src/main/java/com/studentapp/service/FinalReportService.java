package com.studentapp.service;

import com.studentapp.model.FinalReport;
import com.studentapp.repository.FinalReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FinalReportService {

    private static final Logger logger = LoggerFactory.getLogger(FinalReportService.class);

    private final FinalReportRepository reportRepository;

    public FinalReportService(FinalReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void saveOrUpdate(FinalReport report) {
        logger.info("Saving/Updating final report for studentId={} via sp_save_or_update_final_report", report.getStudentId());
        reportRepository.saveOrUpdateFinalReportSP(
                report.getStudentId(),
                report.getStudentName(),
                report.getAverageMarks(),
                report.getGrade(),
                report.getPercentile()
        );
        logger.info("Stored procedure sp_save_or_update_final_report executed for studentId={}", report.getStudentId());
    }

    public Optional<FinalReport> getByStudentId(Long studentId) {
        logger.debug("Fetching final report by studentId={} using repository", studentId);
        Optional<FinalReport> result = reportRepository.findByStudentId(studentId);
        logger.info("Final report {} for studentId={}", result.isPresent() ? "found" : "not found", studentId);
        return result;
    }

    public List<FinalReport> getAllReports() {
        logger.debug("Fetching all active final reports via sp_get_all_active_final_reports");
        List<FinalReport> reports = reportRepository.getAllActiveReportsSP();
        logger.info("Fetched {} active final reports", reports.size());
        return reports;
    }
}