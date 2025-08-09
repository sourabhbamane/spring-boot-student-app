package com.studentapp.service;

import com.studentapp.model.FinalReport;
import com.studentapp.model.Student;
import com.studentapp.model.StudentMarks;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GradeCalculationScheduler {

    private final StudentService studentService;
    private final MarksService marksService;
    private final FinalReportService reportService;

    public GradeCalculationScheduler(StudentService studentService,
                                     MarksService marksService,
                                     FinalReportService reportService) {
        this.studentService = studentService;
        this.marksService = marksService;
        this.reportService = reportService;
    }

    // 🔁 Scheduled job: calculates grades for all students once a day at 1 AM
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void calculateGradesAndStoreReports() {
        log.info("🔁 Scheduled grade calculation triggered");
        recalculateGrades();
    }

    // ✅ Called from scheduler or Kafka consumer to recalculate grades for all students
    public void recalculateGrades() {
        List<Student> students = studentService.getAllStudents();
        List<StudentMarks> allMarks = marksService.getAllActiveMarks();

        // Step 1: Calculate average marks per student
        Map<Long, Double> studentAvgMap = new HashMap<>();
        for (Student student : students) {
            List<StudentMarks> studentMarks = allMarks.stream()
                    .filter(m -> m.getStudentId().equals(student.getStudentId()))
                    .collect(Collectors.toList());

            if (!studentMarks.isEmpty()) {
                double avg = studentMarks.stream().mapToInt(StudentMarks::getMarks).average().orElse(0.0);
                studentAvgMap.put(student.getStudentId(), avg);
            }
        }

        // Step 2: Sort by average and compute percentile
        List<Map.Entry<Long, Double>> sortedList = new ArrayList<>(studentAvgMap.entrySet());
        sortedList.sort(Map.Entry.comparingByValue());

        int totalStudents = sortedList.size();

        Map<Long, Integer> studentPercentileMap = new HashMap<>();
        for (int i = 0; i < sortedList.size(); i++) {
            Long studentId = sortedList.get(i).getKey();
            int percentile = (int) Math.round((i + 1) * 100.0 / totalStudents);
            studentPercentileMap.put(studentId, percentile);
        }

        // Step 3: Store final report
        for (Map.Entry<Long, Double> entry : studentAvgMap.entrySet()) {
            Long studentId = entry.getKey();
            double avg = entry.getValue();
            String grade = getGrade(avg);
            int percentile = studentPercentileMap.get(studentId);

            Student student = students.stream()
                    .filter(s -> s.getStudentId().equals(studentId))
                    .findFirst()
                    .orElse(null);

            if (student != null) {
                FinalReport report = FinalReport.builder()
                        .studentId(studentId)
                        .studentName(student.getName())
                        .averageMarks(avg)
                        .grade(grade)
                        .percentile((double) percentile)
                        .build();

                reportService.saveOrUpdate(report);
            }
        }
    }

    // Trigger recalculation for a single student
    @Transactional
    public void recalculateGradeForStudent(Long studentId) {
        Optional<Student> studentOpt = studentService.getStudentById(studentId);
        if (studentOpt.isEmpty()) {
            log.warn("Student not found for ID: {}", studentId);
            return;
        }

        Student student = studentOpt.get();
        List<StudentMarks> allMarks = marksService.getAllActiveMarks();

        // Compute average for the student
        List<StudentMarks> studentMarks = allMarks.stream()
                .filter(m -> m.getStudentId().equals(studentId))
                .toList();

        if (studentMarks.isEmpty()) {
            log.warn("No marks found for studentId: {}", studentId);
            return;
        }

        double avg = studentMarks.stream().mapToInt(StudentMarks::getMarks).average().orElse(0.0);

        // 🔁 Recalculate percentile from all active marks
        Map<Long, Double> studentAvgMap = new HashMap<>();
        for (Student s : studentService.getAllStudents()) {
            List<StudentMarks> marks = allMarks.stream()
                    .filter(m -> m.getStudentId().equals(s.getStudentId()))
                    .toList();
            if (!marks.isEmpty()) {
                double a = marks.stream().mapToInt(StudentMarks::getMarks).average().orElse(0.0);
                studentAvgMap.put(s.getStudentId(), a);
            }
        }

        List<Map.Entry<Long, Double>> sortedList = new ArrayList<>(studentAvgMap.entrySet());
        sortedList.sort(Map.Entry.comparingByValue());

        int totalStudents = sortedList.size();
        int percentile = 0;

        for (int i = 0; i < sortedList.size(); i++) {
            if (sortedList.get(i).getKey().equals(studentId)) {
                percentile = (int) Math.round((i + 1) * 100.0 / totalStudents);
                break;
            }
        }

        String grade = getGrade(avg);

        FinalReport report = FinalReport.builder()
                .studentId(student.getStudentId())
                .studentName(student.getName())
                .averageMarks(avg)
                .grade(grade)
                .percentile((double) percentile)
                .build();

        reportService.saveOrUpdate(report);
        log.info("📊 Updated final report for studentId {}: avg={}, grade={}, percentile={}",
                studentId, avg, grade, percentile);
    }

    private String getGrade(double avg) {
        if (avg >= 85) return "A";
        else if (avg >= 70) return "B";
        else if (avg >= 55) return "C";
        else return "D";
    }
}