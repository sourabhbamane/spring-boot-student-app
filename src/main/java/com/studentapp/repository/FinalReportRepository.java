package com.studentapp.repository;

import com.studentapp.model.FinalReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FinalReportRepository extends JpaRepository<FinalReport, Long> {

    Optional<FinalReport> findByStudentId(Long studentId);

    @Query("SELECT fr FROM FinalReport fr JOIN Student s ON fr.studentId = s.studentId WHERE s.deleteFlag = false")
    List<FinalReport> findAllActiveReports();
}