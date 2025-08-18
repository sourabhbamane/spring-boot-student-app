package com.studentapp.repository;

import com.studentapp.model.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {

    @Query(value = "EXEC sp_get_course_ids_by_student :student_id", nativeQuery = true)
    List<Integer> getCourseIdsByStudentSP(@Param("student_id") Long studentId);

    @Procedure(procedureName = "sp_enroll_student_course")
    void enrollStudentCourseSP(@Param("student_id") Long studentId,
                               @Param("course_id") Integer courseId,
                               @Param("created_by") String createdBy);

    @Procedure(procedureName = "sp_soft_delete_student_course")
    void softDeleteStudentCourseSP(@Param("id") Long id,
                                   @Param("modified_by") String modifiedBy);

    @Query(value = "EXEC sp_get_courses_with_details_by_student :student_id", nativeQuery = true)
    List<Object[]> getCoursesWithDetailsSP(@Param("student_id") Long studentId);
}