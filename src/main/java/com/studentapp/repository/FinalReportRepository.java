package com.studentapp.repository;

import com.studentapp.model.FinalReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FinalReportRepository extends JpaRepository<FinalReport, Long> {

    Optional<FinalReport> findByStudentId(Long studentId);
}