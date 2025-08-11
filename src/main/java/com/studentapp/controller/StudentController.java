package com.studentapp.controller;

import com.studentapp.model.Student;
import com.studentapp.model.Course;
import com.studentapp.model.StudentMarks;
import com.studentapp.service.StudentService;
import com.studentapp.service.CourseService;
import com.studentapp.service.MarksService;
import com.studentapp.service.UserQueryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final MarksService marksService;
    private final UserQueryService userQueryService;

    public StudentController(StudentService studentService,
                             CourseService courseService,
                             MarksService marksService,
                             UserQueryService userQueryService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.marksService = marksService;
        this.userQueryService = userQueryService;
    }

    @GetMapping("/dashboard")
    public String showStudentDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Optional<Student> studentOpt = userQueryService.getStudentByUsername(userDetails.getUsername());
        if (studentOpt.isEmpty()) {
            model.addAttribute("error", "Student not found.");
            return "login";
        }

        Student student = studentOpt.get();
        Long studentId = student.getStudentId();
        String formattedDob = student.getDob().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        model.addAttribute("formattedDob", formattedDob);

        model.addAttribute("student", student);
        model.addAttribute("enrolledCourses", courseService.getCoursesByStudentId(studentId));
        model.addAttribute("studentMarks", marksService.getMarksByStudentId(studentId));

        return "student/dashboard";
    }

    @GetMapping("/myProfile")
    public String viewMyProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Optional<Student> studentOpt = userQueryService.getStudentByUsername(userDetails.getUsername());
        if (studentOpt.isPresent()) {
            model.addAttribute("student", studentOpt.get());
            return "studentProfile";
        } else {
            model.addAttribute("error", "Student not found.");
            return "login";
        }
    }

    @GetMapping("/courses")
    public String viewEnrolledCourses(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Optional<Student> studentOpt = userQueryService.getStudentByUsername(userDetails.getUsername());
        if (studentOpt.isEmpty()) {
            model.addAttribute("error", "Student not found.");
            return "login";
        }

        Long studentId = studentOpt.get().getStudentId();
        List<Course> enrolledCourses = courseService.getCoursesByStudentId(studentId);
        model.addAttribute("courses", enrolledCourses);
        return "student/enrolledCourse";
    }

    @GetMapping("/marks")
    public String viewMyMarks(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Optional<Student> studentOpt = userQueryService.getStudentByUsername(userDetails.getUsername());
        if (studentOpt.isEmpty()) {
            model.addAttribute("error", "Student not found.");
            return "login";
        }

        Long studentId = studentOpt.get().getStudentId();
        List<StudentMarks> marks = marksService.getMarksByStudentId(studentId);
        model.addAttribute("studentMarks", marks);
        return "studentMarks";
    }

    @PostMapping("/enroll")
    public String enrollCourses(@RequestParam("courseIds") List<Integer> courseIds,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        Optional<Student> studentOpt = userQueryService.getStudentByUsername(userDetails.getUsername());
        if (studentOpt.isEmpty()) {
            model.addAttribute("error", "Student not found.");
            return "login";
        }

        Long studentId = studentOpt.get().getStudentId();
        String currentUser = userDetails.getUsername();
        int enrolled = 0, skipped = 0;

        for (Integer courseId : courseIds) {
            if (!courseService.isStudentEnrolled(studentId, courseId)) {
                courseService.enrollStudent(studentId, courseId, currentUser);
                enrolled++;
            } else {
                skipped++;
            }
        }

        model.addAttribute("message", "Enrolled in " + enrolled + " course(s). " +
                (skipped > 0 ? skipped + " already enrolled." : ""));
        return "redirect:/student/dashboard";
    }

    @GetMapping("/editProfile")
    public String showEditProfileForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Optional<Student> studentOpt = userQueryService.getStudentByUsername(userDetails.getUsername());
        if (studentOpt.isPresent()) {
            model.addAttribute("student", studentOpt.get());
            return "student/editStudentProfile";
        } else {
            model.addAttribute("error", "Student not found.");
            return "login";
        }
    }

    @PostMapping("/updateProfile")
    public String updateStudentProfile(@Valid @ModelAttribute("student") Student student,
                                       BindingResult result,
                                       @AuthenticationPrincipal UserDetails userDetails,
                                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("student", student);
            return "student/editStudentProfile";
        }

        studentService.updateStudent(student, userDetails.getUsername());
        model.addAttribute("message", "Profile updated successfully.");
        return "redirect:/student/dashboard";
    }
}
