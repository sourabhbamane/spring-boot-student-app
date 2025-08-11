package com.studentapp.controller;

import com.studentapp.model.Course;
import com.studentapp.model.EnrollmentForm;
import com.studentapp.model.Student;
import com.studentapp.service.CourseService;
import com.studentapp.service.StudentService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/courses")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    // List all courses
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "admin/courses";
    }

    // Show form to add a course
    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "admin/addCourse";
    }

    // Save course
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveCourse(
            @ModelAttribute("course") @Valid Course course,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("error", "Please correct the form errors.");
            return "admin/addCourse";
        }

        if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
            model.addAttribute("error", "Course name is required.");
            return "admin/addCourse";
        }

        String currentUser = (userDetails != null) ? userDetails.getUsername() : "system";

        try {
            courseService.saveCourse(course, currentUser);
            logger.info("Course saved successfully: {}", course.getCourseName());
            redirectAttributes.addFlashAttribute("success", "Course saved successfully!");
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            logger.error("Failed to save course: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to save course: " + e.getMessage());
            return "admin/addCourse";
        }
    }

    // Edit course
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editCourse(@PathVariable int id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id));
        return "admin/addCourse";
    }

    // Soft delete course
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCourse(@PathVariable int id,
                               @AuthenticationPrincipal UserDetails userDetails) {
        String currentUser = (userDetails != null) ? userDetails.getUsername() : "system";
        courseService.softDeleteCourse(id, currentUser);
        return "redirect:/admin/courses";
    }

    // Show enrollment form
    @GetMapping("/enroll")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEnrollmentForm(Model model) {
        List<Student> students = studentService.getAllStudents();
        List<Course> courses = courseService.getAllCourses();

        logger.debug("Loaded {} students and {} courses for enrollment form", students.size(), courses.size());

        model.addAttribute("students", students);
        model.addAttribute("courses", courses);
        model.addAttribute("enrollmentForm", new EnrollmentForm());

        return "admin/enrollStudent";
    }

    // Enroll student in selected courses
    @PostMapping("/enroll")
    @PreAuthorize("hasRole('ADMIN')")
    public String enrollStudent(
            @ModelAttribute("enrollmentForm") @Valid EnrollmentForm enrollmentForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("courses", courseService.getAllCourses());
            return "admin/enrollStudent";
        }

        Student student = studentService.getStudentById(enrollmentForm.getStudentId()).orElse(null);
        if (student == null) {
            logger.error("Invalid studentId: {}", enrollmentForm.getStudentId());
            model.addAttribute("error", "Invalid student selected.");
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("courses", courseService.getAllCourses());
            return "admin/enrollStudent";
        }

        String currentUser = (userDetails != null) ? userDetails.getUsername() : "system";
        int enrolledCount = 0;
        int skippedCount = 0;

        for (Integer courseId : enrollmentForm.getCourseIds()) {
            Optional<Course> course = courseService.getCourseById(courseId);
            if (course.isEmpty()) continue;

            if (courseService.isStudentEnrolled(enrollmentForm.getStudentId(), courseId)) {
                skippedCount++;
                continue;
            }

            courseService.enrollStudent(enrollmentForm.getStudentId(), courseId, currentUser);
            enrolledCount++;
        }

        logger.info("Enrolled student {} in {} courses, skipped {} duplicates", enrollmentForm.getStudentId(), enrolledCount, skippedCount);
        redirectAttributes.addFlashAttribute("success",
                "Enrolled student in " + enrolledCount + " course(s). " +
                        (skippedCount > 0 ? skippedCount + " duplicate enrollment(s) skipped." : "")
        );

        return "redirect:/admin/dashboard";
    }
}