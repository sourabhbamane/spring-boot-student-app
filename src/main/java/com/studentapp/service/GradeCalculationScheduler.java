package com.studentapp.service;

import com.studentapp.model.FinalReport;
import com.studentapp.model.Student;
import com.studentapp.model.StudentMarks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void calculateGradesAndStoreReports() {
        log.info("🔁 Scheduled grade calculation triggered at {}", LocalDateTime.now());
        recalculateGrades();
    }

//    public void recalculateGrades() {
//        log.info("🔁 Starting grade recalculation");
//        List<Student> students = studentService.getAllStudents();
//        List<StudentMarks> allMarks = marksService.getAllActiveMarks();
//
//        // Step 1: Calculate average marks per student across all courses
//        Map<Long, Double> studentAvgMap = allMarks.stream()
//                .filter(m -> !m.getDeleteFlag()) // Exclude deleted marks
//                .collect(Collectors.groupingBy(
//                        StudentMarks::getStudentId,
//                        Collectors.averagingInt(StudentMarks::getMarks)
//                ));
//        log.info("Student averages calculated: {}", studentAvgMap);
//
//        // Step 2: Sort by average and compute percentile
//        List<Map.Entry<Long, Double>> sortedList = new ArrayList<>(studentAvgMap.entrySet());
//        sortedList.sort(Map.Entry.comparingByValue());
//
//        int totalStudents = sortedList.size();
//        Map<Long, Integer> studentPercentileMap = new HashMap<>();
//        for (int i = 0; i < sortedList.size(); i++) {
//            Long studentId = sortedList.get(i).getKey();
//            int percentile = (int) Math.round((i + 1) * 100.0 / totalStudents);
//            studentPercentileMap.put(studentId, percentile);
//        }
//
//        // Step 3: Update or create final report - one entry per student
//        for (Map.Entry<Long, Double> entry : studentAvgMap.entrySet()) {
//            Long studentId = entry.getKey();
//            double avg = entry.getValue();
//            String grade = getGrade(avg);
//            int percentile = studentPercentileMap.get(studentId);
//
//            Student student = students.stream()
//                    .filter(s -> s.getStudentId().equals(studentId))
//                    .findFirst()
//                    .orElse(null);
//
//            if (student != null) {
//                log.info("Processing studentId: {}, avg: {}, grade: {}, percentile: {}", studentId, avg, grade, percentile);
//                Optional<FinalReport> existingReport = reportService.getByStudentId(studentId);
//                FinalReport report = existingReport.orElseGet(() -> FinalReport.builder()
//                        .studentId(studentId)
//                        .studentName(student.getName())
//                        .build());
//                report.setAverageMarks(avg);
//                report.setGrade(grade);
//                report.setPercentile((double) percentile);
//                report.setUpdatedOn(LocalDateTime.now()); // Ensure updated timestamp
//
//                reportService.saveOrUpdate(report);
//                log.info("Saved/Updated report for studentId: {}", studentId);
//            }
//        }
//        log.info("🔁 Grade recalculation completed");
//    }

//    public void recalculateGrades() {
//        log.info("🔁 Starting grade recalculation");
//        List<Student> students = studentService.getAllStudents();
//        List<StudentMarks> allMarks = marksService.getAllActiveMarks();
//
//        // Step 1: Calculate average marks per student
//        Map<Long, Double> studentAvgMap = allMarks.stream()
//                .filter(m -> !m.getDeleteFlag())
//                .collect(Collectors.groupingBy(
//                        StudentMarks::getStudentId,
//                        Collectors.averagingInt(StudentMarks::getMarks)
//                ));
//        log.info("Student averages: {}", studentAvgMap);
//
//        // Step 2: Sort by average and compute percentile
//        List<Map.Entry<Long, Double>> sortedList = new ArrayList<>(studentAvgMap.entrySet());
//        sortedList.sort(Map.Entry.comparingByValue());
//        log.info("Sorted averages: {}", sortedList);
//        int totalStudents = sortedList.size();
//        Map<Long, Integer> studentPercentileMap = new HashMap<>();
//        for (int i = 0; i < sortedList.size(); i++) {
//            Long studentId = sortedList.get(i).getKey();
//            int percentile = (int) Math.round((i + 1) * 100.0 / totalStudents);
//            studentPercentileMap.put(studentId, percentile);
//            log.info("StudentId: {}, Rank: {}, Percentile: {}", studentId, i + 1, percentile);
//        }
//
//        // Step 3: Store final report
//        for (Map.Entry<Long, Double> entry : studentAvgMap.entrySet()) {
//            Long studentId = entry.getKey();
//            double avg = entry.getValue();
//            String grade = getGrade(avg);
//            int percentile = studentPercentileMap.get(studentId);
//
//            Student student = students.stream()
//                    .filter(s -> s.getStudentId().equals(studentId))
//                    .findFirst()
//                    .orElse(null);
//
//            if (student != null) {
//                log.info("Processing studentId: {}, avg: {}, grade: {}, percentile: {}", studentId, avg, grade, percentile);
//                Optional<FinalReport> existingReport = reportService.getByStudentId(studentId);
//                FinalReport report = existingReport.orElseGet(() -> FinalReport.builder()
//                        .studentId(studentId)
//                        .studentName(student.getName())
//                        .build());
//                report.setAverageMarks(avg);
//                report.setGrade(grade);
//                report.setPercentile((double) percentile);
//                report.setUpdatedOn(LocalDateTime.now());
//
//                reportService.saveOrUpdate(report);
//                log.info("Saved/Updated report for studentId: {}", studentId);
//            }
//        }
//        log.info("🔁 Grade recalculation completed");
//    }

    public void recalculateGrades() {
        log.info("🔁 Starting grade recalculation");
        List<Student> students = studentService.getAllStudents();
        List<StudentMarks> allMarks = marksService.getAllActiveMarks();

        // Step 1: Calculate average marks per student
        Map<Long, Double> studentAvgMap = allMarks.stream()
                .filter(m -> !m.isDeleteFlag())
                .collect(Collectors.groupingBy(
                        StudentMarks::getStudentId,
                        Collectors.averagingInt(StudentMarks::getMarks)
                ));
        log.info("Student averages: {}", studentAvgMap);

        // Step 2: Sort by average and compute percentile
        List<Map.Entry<Long, Double>> sortedList = new ArrayList<>(studentAvgMap.entrySet());
        sortedList.sort(Map.Entry.comparingByValue());
        log.info("Sorted averages: {}", sortedList);
        int totalStudents = sortedList.size();
        Map<Long, Integer> studentPercentileMap = new HashMap<>();
        for (int i = 0; i < sortedList.size(); i++) {
            Long studentId = sortedList.get(i).getKey();
            int percentile = (int) Math.round((i + 1) * 100.0 / totalStudents);
            studentPercentileMap.put(studentId, percentile);
            log.info("Mapped StudentId: {}, Average: {}, Rank: {}, Percentile: {}",
                    studentId, sortedList.get(i).getValue(), i + 1, percentile);
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
                log.info("Processing studentId: {}, avg: {}, grade: {}, percentile: {}", studentId, avg, grade, percentile);
                Optional<FinalReport> existingReport = reportService.getByStudentId(studentId);
                FinalReport report = existingReport.orElseGet(() -> FinalReport.builder()
                        .studentId(studentId)
                        .studentName(student.getName())
                        .build());
                report.setAverageMarks(avg);
                report.setGrade(grade);
                report.setPercentile((double) percentile);
                report.setUpdatedOn(LocalDateTime.now());

                reportService.saveOrUpdate(report);
                log.info("Saved/Updated report for studentId: {}", studentId);
            }
        }
        log.info("🔁 Grade recalculation completed");
    }

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

        // Recalculate percentile from all active marks
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