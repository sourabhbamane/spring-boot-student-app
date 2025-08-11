package com.studentapp.repository;

import com.studentapp.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    // All active courses (not soft-deleted)
    List<Course> findByDeleteFlagFalse();

    // Find a single active course by its PK property 'courseId'
    Optional<Course> findByCourseIdAndDeleteFlagFalse(Integer courseId);

    // Find multiple courses by ids and not deleted
    List<Course> findByCourseIdInAndDeleteFlagFalse(List<Integer> ids);
    List<Course> findAllByDeleteFlagFalse();

}