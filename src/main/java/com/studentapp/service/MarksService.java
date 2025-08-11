package com.studentapp.service;

import com.studentapp.model.StudentMarks;
import com.studentapp.repository.StudentMarksRepository;
import com.studentapp.repository.StudentRepository;
import com.studentapp.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MarksService {

    private static final Logger logger = LoggerFactory.getLogger(MarksService.class);

    private final StudentMarksRepository marksRepository;
    private final StudentRepository studentRepository; // optional, for extra validation
    private final CourseRepository courseRepository;   // optional, for extra validation

    public MarksService(StudentMarksRepository marksRepository,
                        StudentRepository studentRepository,
                        CourseRepository courseRepository) {
        this.marksRepository = marksRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Save or update a StudentMarks record. Returns the saved entity.
     */
    @Transactional
    public StudentMarks saveOrUpdateMarks(StudentMarks marks, String user) {
        if (marks == null) {
            logger.error("Attempted to save null StudentMarks object");
            throw new IllegalArgumentException("StudentMarks cannot be null");
        }
        if (marks.getStudentId() == null || marks.getCourseId() == null || marks.getMarks() == null) {
            logger.error("Invalid StudentMarks fields: studentId={}, courseId={}, marks={}",
                    marks.getStudentId(), marks.getCourseId(), marks.getMarks());
            throw new IllegalArgumentException("Student ID, Course ID, and Marks must be set");
        }

        // Validate existence (optional but recommended)
        if (!studentRepository.findById(marks.getStudentId()).isPresent()) {
            logger.error("Student not found with id={}", marks.getStudentId());
            throw new IllegalArgumentException("Student not found: " + marks.getStudentId());
        }
        if (!courseRepository.findById(marks.getCourseId()).isPresent()) {
            logger.error("Course not found with id={}", marks.getCourseId());
            throw new IllegalArgumentException("Course not found: " + marks.getCourseId());
        }

        String effectiveUser = (user != null && !user.trim().isEmpty()) ? user : "system";

        // Check if this is an update operation
        if (marks.getId() != null && marks.getId() > 0L) {
            // This is an update - preserve original creation data
            Optional<StudentMarks> existingOpt = marksRepository.findById(marks.getId());
            if (existingOpt.isPresent()) {
                StudentMarks existing = existingOpt.get();

                // Preserve original creation data (use correct field names)
                marks.setCreatedBy(existing.getCreatedBy());
                marks.setCreatedOn(existing.getCreatedOn());
                marks.setDeleteFlag(existing.isDeleteFlag()); // Preserve delete flag

                // Set update metadata (use correct field names)
                marks.setModifiedBy(effectiveUser);
                marks.setModifiedOn(LocalDateTime.now());

                logger.info("Updating marks ID {} by {}", marks.getId(), effectiveUser);
            }
        } else {
            // This is a new record - set creation data
            marks.setCreatedBy(effectiveUser);
            marks.setCreatedOn(LocalDateTime.now()); // Use correct field name
            marks.setDeleteFlag(false);
            marks.setModifiedBy(effectiveUser);
            marks.setModifiedOn(LocalDateTime.now());

            logger.info("Inserting new marks for studentId={} and courseId={}", marks.getStudentId(), marks.getCourseId());
        }

        logger.debug("Saving marks: studentId={}, courseId={}, marks={}",
                marks.getStudentId(), marks.getCourseId(), marks.getMarks());

        StudentMarks saved = marksRepository.save(marks);

        logger.info("Marks saved successfully for studentId={} and courseId={}",
                saved.getStudentId(), saved.getCourseId());

        return saved;
    }
//    @Transactional
//    public StudentMarks saveOrUpdateMarks(StudentMarks marks, String user) {
//        if (marks == null) {
//            logger.error("Attempted to save null StudentMarks object");
//            throw new IllegalArgumentException("StudentMarks cannot be null");
//        }
//        if (marks.getStudentId() == null || marks.getCourseId() == null || marks.getMarks() == null) {
//            logger.error("Invalid StudentMarks fields: studentId={}, courseId={}, marks={}",
//                    marks.getStudentId(), marks.getCourseId(), marks.getMarks());
//            throw new IllegalArgumentException("Student ID, Course ID, and Marks must be set");
//        }
//
//        // Validate existence (optional but recommended)
//        if (!studentRepository.findById(marks.getStudentId()).isPresent()) {
//            logger.error("Student not found with id={}", marks.getStudentId());
//            throw new IllegalArgumentException("Student not found: " + marks.getStudentId());
//        }
//        if (!courseRepository.findById(marks.getCourseId()).isPresent()) {
//            logger.error("Course not found with id={}", marks.getCourseId());
//            throw new IllegalArgumentException("Course not found: " + marks.getCourseId());
//        }
//
//        String effectiveUser = (user != null && !user.trim().isEmpty()) ? user : "system";
//
//        // Determine new vs update — prefer checking null id if your entity uses Long
//        if (marks.getId() == null || marks.getId() == 0L) {
//            marks.setCreatedBy(effectiveUser);
//            marks.setCreatedOn(LocalDateTime.now());
//            marks.setDeleteFlag(false);
//            logger.info("Inserting new marks for studentId={} and courseId={}", marks.getStudentId(), marks.getCourseId());
//        } else {
//            marks.setModifiedBy(effectiveUser);
//            marks.setModifiedOn(LocalDateTime.now());
//            logger.info("Updating marks ID {} by {}", marks.getId(), effectiveUser);
//        }
//
//        logger.debug("Saving marks: studentId={}, courseId={}, marks={}",
//                marks.getStudentId(), marks.getCourseId(), marks.getMarks());
//
//        StudentMarks saved = marksRepository.save(marks);
//
//        logger.info("Marks saved successfully for studentId={} and courseId={}",
//                saved.getStudentId(), saved.getCourseId());
//
//        return saved;
//    }

    @Transactional(readOnly = true)
    public Optional<StudentMarks> getMarksById(Long id) {
        if (id == null) return Optional.empty();
        logger.debug("Fetching marks by ID: {}", id);
        return marksRepository.findById(id)
                .filter(m -> !m.getCourse().isDeleteFlag());
    }

    @Transactional(readOnly = true)
    public List<StudentMarks> getAllMarks() {
        logger.debug("Fetching all active student marks");
        return marksRepository.findAllActive();
    }

    @Transactional(readOnly = true)
    public List<StudentMarks> getMarksByStudentId(Long studentId) {
        logger.debug("Fetching marks for student ID: {}", studentId);
        return marksRepository.findByStudentIdAndDeleteFlagFalse(studentId);
    }

//    @Transactional
//    public void deleteMarks(Long id, String user) {
//        if (id == null) {
//            logger.warn("deleteMarks called with null id");
//            return;
//        }
//        logger.info("Soft deleting marks ID {} by {}", id, user);
//        marksRepository.findById(id).ifPresent(m -> {
//            m.setDeleteFlag(true);
//            String effectiveUser = (user != null && !user.trim().isEmpty()) ? user : "system";
//            m.setModifiedBy(effectiveUser);
//            m.setModifiedOn(new Date());
//            marksRepository.save(m);
//            logger.info("Marks soft-deleted: id={}", id);
//        });
//    }

    @Transactional(readOnly = true)
    public Optional<StudentMarks> getMarksByStudentIdAndCourseId(Long studentId, Integer courseId) {
        logger.debug("Fetching marks for student ID {} and course ID {}", studentId, courseId);
        return marksRepository.findByStudentIdAndCourseIdAndDeleteFlagFalse(studentId, courseId);
    }
    @Transactional(readOnly = true)
    public Page<StudentMarks> getMarksPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdOn"));
        return marksRepository.findByDeleteFlagFalse(pageable);
    }

    /// /
    public StudentMarks saveMarks(StudentMarks studentMarks, String createdBy) {
        logger.info("Saving marks for student ID: {}, course ID: {}", studentMarks.getStudentId(), studentMarks.getCourseId());
        studentMarks.setCreatedBy(createdBy);
        studentMarks.setModifiedBy(createdBy);
        return marksRepository.save(studentMarks);
    }

    public Optional<StudentMarks> findById(Long id) {
        logger.debug("Finding marks by ID: {}", id);
        return marksRepository.findById(id);
    }

    public List<StudentMarks> findByStudentId(Long studentId) {
        logger.debug("Finding marks for student ID: {}", studentId);
        return marksRepository.findByStudentIdAndDeleteFlagFalse(studentId);
    }

    public Page<StudentMarks> findAll(int page, int size) {
        logger.debug("Finding all marks, page: {}, size: {}", page, size);
        return marksRepository.findAll(PageRequest.of(page, size));
    }

    public void deleteMarks(Long id, String modifiedBy) {
        logger.info("Deleting marks ID: {}", id);
        marksRepository.findById(id).ifPresent(marks -> {
            marks.setDeleteFlag(true);
            marks.setModifiedBy(modifiedBy);
            marksRepository.save(marks);
        });
    }

    public List<StudentMarks> getAllActiveMarks() {
        return marksRepository.findByDeleteFlagFalse();
    }
}