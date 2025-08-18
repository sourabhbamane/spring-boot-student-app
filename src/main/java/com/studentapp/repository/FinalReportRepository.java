package com.studentapp.repository;

import com.studentapp.model.FinalReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FinalReportRepository extends JpaRepository<FinalReport, Long> {

    Optional<FinalReport> findByStudentId(Long studentId);

    @Query(value = "EXEC sp_get_all_active_final_reports", nativeQuery = true)
    List<FinalReport> getAllActiveReportsSP();

    @Procedure(procedureName = "sp_save_or_update_final_report")
    void saveOrUpdateFinalReportSP(
            @Param("student_id") Long studentId,
            @Param("student_name") String studentName,
            @Param("average_marks") Double averageMarks,
            @Param("grade") String grade,
            @Param("percentile") Double percentile
    );
}