package com.studentapp.service;

import com.studentapp.model.Course;
import com.studentapp.model.CourseDTO;
import com.studentapp.repository.CourseRepository;
import com.studentapp.repository.StudentCourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;
    private final StudentCourseRepository studentCourseRepository;

    public CourseService(CourseRepository courseRepository,
                         StudentCourseRepository studentCourseRepository) {
        this.courseRepository = courseRepository;
        this.studentCourseRepository = studentCourseRepository;
    }

    @Transactional(readOnly = true)
    public List<Course> getCoursesByStudentId(Long studentId) {
        if (studentId == null) {
            logger.warn("getCoursesByStudentId called with null studentId");
            return List.of();
        }
        logger.debug("Getting enrolled courses for studentId: {}", studentId);
        List<Integer> courseIds = studentCourseRepository.findCourseIdsByStudentId(studentId);
        if (courseIds == null || courseIds.isEmpty()) {
            logger.info("No enrolled courses found for studentId={}", studentId);
            return List.of();
        }
        List<Course> courses = courseRepository.findByCourseIdInAndDeleteFlagFalse(courseIds);
        logger.info("Enrolled courses found for studentId={}: {}", studentId, courses.size());
        return courses;
    }

    @Transactional(readOnly = true)
    public Optional<Course> getCourseById(Integer id) {
        if (id == null) {
            logger.warn("getCourseById called with null id");
            return Optional.empty();
        }
        logger.debug("Fetching course with ID: {}", id);
        return courseRepository.findByCourseIdAndDeleteFlagFalse(id);
    }

    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        logger.debug("Fetching all active courses");
        return courseRepository.findByDeleteFlagFalse();
    }

    @Transactional
    public void softDeleteCourse(Integer courseId, String currentUser) {
        if (courseId == null) {
            logger.warn("softDeleteCourse called with null courseId");
            return;
        }
        logger.info("Soft deleting course ID {} by {}", courseId, currentUser);
        courseRepository.findByCourseIdAndDeleteFlagFalse(courseId).ifPresent(course -> {
            course.setDeleteFlag(true);
            course.setModifiedBy(currentUser == null ? "system" : currentUser);
            course.setModifiedOn(LocalDateTime.now());
            courseRepository.save(course);
            logger.info("Course soft-deleted: id={}", courseId);
        });
    }

    @Transactional(readOnly = true)
    public boolean isStudentEnrolled(Long studentId, Integer courseId) {
        if (studentId == null || courseId == null) {
            logger.debug("isStudentEnrolled called with null argument(s) studentId={}, courseId={}", studentId, courseId);
            return false;
        }
        boolean enrolled = studentCourseRepository.existsByStudentIdAndCourseIdAndDeleteFlagFalse(studentId, courseId);
        logger.debug("StudentId={} is {} in courseId={}", studentId, enrolled ? "enrolled" : "not enrolled", courseId);
        return enrolled;
    }

    @Transactional
    public void enrollStudent(Long studentId, Integer courseId, String createdBy) {
        if (studentId == null || courseId == null) {
            logger.warn("enrollStudent called with null arguments studentId={}, courseId={}", studentId, courseId);
            return;
        }
        // avoid duplicate enrollment
        if (studentCourseRepository.existsByStudentIdAndCourseIdAndDeleteFlagFalse(studentId, courseId)) {
            logger.info("Student {} already enrolled in course {}", studentId, courseId);
            return;
        }

        var sc = new com.studentapp.model.StudentCourse();
        sc.setStudentId(studentId);
        sc.setCourseId(courseId);
        sc.setCreatedBy(createdBy == null ? "system" : createdBy);
        sc.setCreatedOn(LocalDateTime.now());
        sc.setDeleteFlag(false);

        studentCourseRepository.save(sc);
        logger.info("Enrolled studentId={} in courseId={} by {}", studentId, courseId, createdBy);
    }

    @Transactional(readOnly = true)
    public List<CourseDTO> getEnrolledCourses(Long studentId) {
        // returns DTOs (id + name) for UI dropdowns
        List<Course> courses = getCoursesByStudentId(studentId);
        return courses.stream()
                .map(c -> new CourseDTO(c.getCourseId(), c.getCourseName()))
                .collect(Collectors.toList());
    }

//    @Transactional
//    public Course saveCourse(Course course, String currentUser) {
//        if (course == null) {
//            logger.error("Attempted to save null Course object");
//            throw new IllegalArgumentException("Course cannot be null");
//        }
//        if (course.getName() == null || course.getName().trim().isEmpty()) {
//            logger.error("Course name is null or empty");
//            throw new IllegalArgumentException("Course name is required");
//        }
//        String user = (currentUser != null && !currentUser.trim().isEmpty()) ? currentUser : "system";
//
//        if (course.getCourseId() == null) {
//            course.setCreatedBy(user);
//            course.setCreatedOn(new Date());
//            course.setDeleteFlag(false);
//            logger.info("Inserting new course by {}", user);
//        } else {
//            course.setModifiedBy(user);
//            course.setModifiedOn(new Date());
//            logger.info("Updating course ID {} by {}", course.getCourseId(), user);
//        }
//
//        Course saved = courseRepository.save(course);
//        logger.info("Course saved successfully: {}", saved.getName());
//        return saved;
//    }
//

    ///
    public Course saveCourse(Course course, String createdBy) {
        logger.info("Saving course: {}", course.getCourseName());
        course.setCreatedBy(createdBy);
        course.setModifiedBy(createdBy);
        return courseRepository.save(course);
    }

    public Optional<Course> findById(Integer id) {
        logger.debug("Finding course by ID: {}", id);
        return courseRepository.findById(id);
    }

    public List<Course> findAll() {
        logger.debug("Finding all courses");
        return courseRepository.findAllByDeleteFlagFalse();
    }

    public void deleteCourse(Integer id, String modifiedBy) {
        logger.info("Deleting course ID: {}", id);
        courseRepository.findById(id).ifPresent(course -> {
            course.setDeleteFlag(true);
            course.setModifiedBy(modifiedBy);
            courseRepository.save(course);
        });
    }
}