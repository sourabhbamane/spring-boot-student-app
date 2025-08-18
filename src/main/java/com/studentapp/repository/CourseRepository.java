package com.studentapp.repository;

import com.studentapp.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    // Fetch courses for a student
    @Query(value = "EXEC sp_get_courses_by_student :student_id", nativeQuery = true)
    List<Course> getCoursesByStudentSP(@Param("student_id") Long studentId);

    // Soft delete a course
    @Procedure(procedureName = "sp_soft_delete_course")
    void softDeleteCourseSP(@Param("course_id") Integer courseId,
                            @Param("modified_by") String modifiedBy);

    // Check if student is enrolled
    @Query(value = "EXEC sp_is_student_enrolled :student_id, :course_id", nativeQuery = true)
    boolean isStudentEnrolledSP(@Param("student_id") Long studentId,
                                @Param("course_id") Integer courseId);

    // Enroll student in a course
    @Procedure(procedureName = "sp_enroll_student")
    void enrollStudentSP(@Param("student_id") Long studentId,
                         @Param("course_id") Integer courseId,
                         @Param("created_by") String createdBy);

    // Save course (insert/update)
    @Procedure(procedureName = "sp_save_course")
    void saveCourseSP(@Param("course_id") Integer courseId,
                      @Param("course_name") String courseName,
                      @Param("description") String description,
                      @Param("credits") Integer credits,
                      @Param("user") String user);



    @Query("SELECT COUNT(c) FROM Course c WHERE c.deleteFlag = false")
    long countActiveCourses();


    @Query(value = "EXEC sp_get_all_active_courses", nativeQuery = true)
    List<Course> getAllActiveCoursesSP();
}