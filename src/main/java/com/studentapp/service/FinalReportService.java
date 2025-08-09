package com.studentapp.service;

import com.studentapp.model.FinalReport;
import com.studentapp.repository.FinalReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FinalReportService {

    private final FinalReportRepository reportRepository;

    public FinalReportService(FinalReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public FinalReport saveOrUpdate(FinalReport report) {
        report.setUpdatedOn(LocalDateTime.now());

        if (report.getCreatedOn() == null) {
            report.setCreatedOn(LocalDateTime.now());
        }

        return reportRepository.save(report);
    }

    public Optional<FinalReport> getByStudentId(Long studentId) {
        return reportRepository.findByStudentId(studentId);
    }

    public List<FinalReport> getAllReports() {
        return reportRepository.findAll();
    }
}