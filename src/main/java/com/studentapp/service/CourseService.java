package com.studentapp.service;

import com.studentapp.model.Course;
import com.studentapp.model.CourseDTO;
import com.studentapp.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public List<Course> getCoursesByStudentId(Long studentId) {
        logger.debug("Fetching courses for studentId={} via sp_get_courses_by_student", studentId);
        List<Course> courses = courseRepository.getCoursesByStudentSP(studentId);
        logger.info("Fetched {} courses for studentId={}", courses.size(), studentId);
        return courses;
    }

    @Transactional
    public void softDeleteCourse(Integer courseId, String modifiedBy) {
        logger.info("Soft deleting courseId={} by {}", courseId, modifiedBy);
        courseRepository.softDeleteCourseSP(courseId, modifiedBy);
        logger.info("Stored procedure sp_soft_delete_course executed for courseId={}", courseId);
    }

    @Transactional(readOnly = true)
    public boolean isStudentEnrolled(Long studentId, Integer courseId) {
        logger.debug("Checking enrollment for studentId={} in courseId={} via sp_is_student_enrolled", studentId, courseId);
        boolean enrolled = courseRepository.isStudentEnrolledSP(studentId, courseId);
        logger.info("Enrollment check result for studentId={} in courseId={}: {}", studentId, courseId, enrolled);
        return enrolled;
    }

    @Transactional
    public void enrollStudent(Long studentId, Integer courseId, String createdBy) {
        logger.info("Enrolling studentId={} in courseId={} by {}", studentId, courseId, createdBy);
        courseRepository.enrollStudentSP(studentId, courseId, createdBy);
        logger.info("Stored procedure sp_enroll_student executed for studentId={} courseId={}", studentId, courseId);
    }

    @Transactional
    public void saveCourse(Course course, String user) {
        logger.info("Saving course via sp_save_course: {}", course.getCourseName());
        courseRepository.saveCourseSP(course.getCourseId(),
                course.getCourseName(),
                course.getDescription(),
                course.getCredits(),
                user);
        logger.info("Stored procedure sp_save_course executed for courseId={}", course.getCourseId());
    }

    @Transactional(readOnly = true)
    public Optional<Course> getCourseById(Integer id) {
        logger.debug("Fetching course by ID={} using repository", id);
        return courseRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<CourseDTO> getEnrolledCourses(Long studentId) {
        logger.debug("Fetching enrolled course DTOs for studentId={}", studentId);
        List<CourseDTO> dtos = getCoursesByStudentId(studentId).stream()
                .map(c -> new CourseDTO(c.getCourseId(), c.getCourseName()))
                .collect(Collectors.toList());
        logger.info("Fetched {} enrolled course DTOs for studentId={}", dtos.size(), studentId);
        return dtos;
    }


    @Transactional(readOnly = true)
    public long getActiveCoursesCount() {
        logger.info("Fetching course count");
        return courseRepository.countActiveCourses();
    }


    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        logger.info("Fetching All present courses");
        return courseRepository.getAllActiveCoursesSP();
    }
}