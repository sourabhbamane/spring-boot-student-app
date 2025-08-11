package com.studentapp.repository;

import com.studentapp.model.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {

    List<StudentCourse> findByStudentIdAndDeleteFlagFalse(Long studentId);

    @Query("select sc.courseId from StudentCourse sc where sc.studentId = :studentId and sc.deleteFlag = false")
    List<Integer> findCourseIdsByStudentId(Long studentId);

    boolean existsByStudentIdAndCourseIdAndDeleteFlagFalse(Long studentId, Integer courseId);


}