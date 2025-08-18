package com.studentapp.service;

import com.studentapp.repository.StudentCourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentCourseService {

    private static final Logger logger = LoggerFactory.getLogger(StudentCourseService.class);

    private final StudentCourseRepository repo;

    public StudentCourseService(StudentCourseRepository repo) {
        this.repo = repo;
    }

    public void enrollStudentToCourse(Long studentId, Integer courseId, String createdBy) {
        logger.info("Attempting to enroll studentId={} in courseId={} by {}", studentId, courseId, createdBy);
        repo.enrollStudentCourseSP(studentId, courseId, createdBy);
        logger.info("Stored procedure sp_enroll_student_course executed for studentId={} courseId={}", studentId, courseId);
    }

    public List<Integer> getCourseIdsByStudentId(Long studentId) {
        logger.debug("Fetching course IDs for studentId={} via stored procedure", studentId);
        List<Integer> courseIds = repo.getCourseIdsByStudentSP(studentId);
        logger.info("Fetched {} course IDs for studentId={}", courseIds.size(), studentId);
        return courseIds;
    }

    public void softDeleteEnrollment(Long id, String modifiedBy) {
        logger.info("Soft-deleting enrollmentId={} by {}", id, modifiedBy);
        repo.softDeleteStudentCourseSP(id, modifiedBy);
        logger.info("Stored procedure sp_soft_delete_student_course executed for enrollmentId={}", id);
    }

    public List<Object[]> getCoursesWithDetails(Long studentId) {
        logger.debug("Fetching detailed courses for studentId={} via stored procedure", studentId);
        List<Object[]> courses = repo.getCoursesWithDetailsSP(studentId);
        logger.info("Fetched {} course records with details for studentId={}", courses.size(), studentId);
        return courses;
    }
}