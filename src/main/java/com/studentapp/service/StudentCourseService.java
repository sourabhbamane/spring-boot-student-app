package com.studentapp.service;

import com.studentapp.model.StudentCourse;
import com.studentapp.repository.StudentCourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class StudentCourseService {

    private static final Logger logger = LoggerFactory.getLogger(StudentCourseService.class);

    private final StudentCourseRepository studentCourseRepository;

    public StudentCourseService(StudentCourseRepository studentCourseRepository) {
        this.studentCourseRepository = studentCourseRepository;
    }

   //Enrolls a student to a course (soft-insert). Will skip if already enrolled (delete_flag = false).
    @Transactional
    public StudentCourse enrollStudentToCourse(StudentCourse sc) {
        if (sc == null) {
            logger.warn("enrollStudentToCourse called with null StudentCourse");
            throw new IllegalArgumentException("StudentCourse must not be null");
        }
        Long studentId = sc.getStudentId();
        Integer courseId = sc.getCourseId();

        if (studentId == null || courseId == null) {
            logger.warn("enrollStudentToCourse missing studentId or courseId: studentId={}, courseId={}",
                    studentId, courseId);
            throw new IllegalArgumentException("studentId and courseId are required");
        }

        // Avoid duplicate active enrollment
        boolean exists = studentCourseRepository.existsByStudentIdAndCourseIdAndDeleteFlagFalse(studentId, courseId);
        if (exists) {
            logger.info("Student {} is already enrolled in course {} — skipping insert", studentId, courseId);
            // Optionally return the existing mapping; we'll return null to indicate no new enrollment
            return null;
        }

        // fill audit fields if not present
        if (sc.getCreatedOn() == null) sc.setCreatedOn(LocalDateTime.now());
        if (sc.getCreatedBy() == null || sc.getCreatedBy().trim().isEmpty()) sc.setCreatedBy("system");
        sc.setDeleteFlag(false);

        StudentCourse saved = studentCourseRepository.save(sc);
        logger.info("Enrolled studentId={} in courseId={} (id={})", studentId, courseId, saved.getId());
        return saved;
    }

    //Return all StudentCourse mappings for a student (active ones).
    @Transactional(readOnly = true)
    public List<StudentCourse> getCoursesByStudentId(Long studentId) {
        if (studentId == null) {
            logger.warn("getCoursesByStudentId called with null studentId");
            return List.of();
        }
        logger.debug("Fetching enrolled courses for studentId={}", studentId);
        return studentCourseRepository.findByStudentIdAndDeleteFlagFalse(studentId);
    }

    //Return just courseIds for a student (active enrollments).
    @Transactional(readOnly = true)
    public List<Integer> getCourseIdsByStudentId(Long studentId) {
        if (studentId == null) {
            logger.warn("getCourseIdsByStudentId called with null studentId");
            return List.of();
        }
        return studentCourseRepository.findCourseIdsByStudentId(studentId);
    }

    //Return all mappings (optionally used for admin views).
    @Transactional(readOnly = true)
    public List<StudentCourse> getAllStudentCourseMappings() {
        logger.debug("Fetching all student-course mappings");
        return studentCourseRepository.findAll();
    }

    //Soft-delete an enrollment mapping by id (set deleteFlag = true).
    @Transactional
    public void softDeleteEnrollment(Long mappingId, String modifiedBy) {
        if (mappingId == null) {
            logger.warn("softDeleteEnrollment called with null mappingId");
            return;
        }

        studentCourseRepository.findById(mappingId).ifPresent(sc -> {
            sc.setDeleteFlag(true);
            sc.setModifiedBy(modifiedBy == null ? "system" : modifiedBy);
            sc.setModifiedOn(LocalDateTime.now());
            studentCourseRepository.save(sc);
            logger.info("Soft-deleted enrollment id={} studentId={} courseId={}", mappingId, sc.getStudentId(), sc.getCourseId());
        });
    }

    public StudentCourse saveStudentCourse(StudentCourse studentCourse, String createdBy) {
        logger.info("Enrolling student ID: {} in course ID: {}", studentCourse.getStudentId(), studentCourse.getCourseId());
        studentCourse.setCreatedBy(createdBy);
        studentCourse.setModifiedBy(createdBy);
        return studentCourseRepository.save(studentCourse);
    }

    public List<StudentCourse> findByStudentId(Long studentId) {
        logger.debug("Finding courses for student ID: {}", studentId);
        return studentCourseRepository.findByStudentIdAndDeleteFlagFalse(studentId);
    }
}