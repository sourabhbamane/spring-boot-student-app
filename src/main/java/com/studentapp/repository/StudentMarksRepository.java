package com.studentapp.repository;

import com.studentapp.dto.MarksDTO;
import com.studentapp.model.StudentMarks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentMarksRepository extends JpaRepository<StudentMarks, Long> {
    List<StudentMarks> findByDeleteFlagFalse();
    // find only active (not soft-deleted) marks
    @Query("select m from StudentMarks m where m.deleteFlag = false")
    List<StudentMarks> findAllActive();

    // find marks by student id (active)
    List<StudentMarks> findByStudentIdAndDeleteFlagFalse(Long studentId);

    // find by student + course (active)
    Optional<StudentMarks> findByStudentIdAndCourseIdAndDeleteFlagFalse(Long studentId, Integer courseId);

    // optional: check existence quickly
    boolean existsByStudentIdAndCourseIdAndDeleteFlagFalse(Long studentId, Integer courseId);
    Page<StudentMarks> findByDeleteFlagFalse(Pageable pageable);
    List<StudentMarks> findAllByDeleteFlagFalse();
    //List<StudentMarks> findByDeleteFlagFalse();

    // New query to fetch MarksDTO with joined Student and Course
    @Query("SELECT new com.studentapp.dto.MarksDTO(" +
            "m.id, m.studentId, m.courseId, m.marks, m.student.name, m.course.courseName, m.createdBy, m.createdOn) " +
            "FROM StudentMarks m " +
            "JOIN m.student s " +
            "JOIN m.course c " +
            "WHERE m.deleteFlag = false")
    Page<MarksDTO> findAllMarksWithStudentAndCourse(Pageable pageable);
}