package com.studentapp.controller;

import com.studentapp.model.Student;
import com.studentapp.model.StudentMarks;
import com.studentapp.service.CourseService;
import com.studentapp.service.MarksService;
import com.studentapp.service.StudentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final StudentService studentService;
    private final CourseService courseService;
    private final MarksService marksService;

    @GetMapping("/registerStudent")
    public String showRegisterStudentForm(Model model) {
        logger.info("Serving register student form");
        model.addAttribute("student", new Student());
        model.addAttribute("formAction", "/admin/registerStudent");
        return "register";
    }

    @PostMapping("/registerStudent")
    public String registerStudent(
            @ModelAttribute("student") Student student,
            @RequestParam("userUsername") String username,
            @RequestParam("userPassword") String password,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            String createdBy = principal.getName();
            logger.info("Registering student: {}, username: {}", student.getName(), username);
            studentService.saveStudentWithUser(student, username, password, createdBy);
            redirectAttributes.addFlashAttribute("message", "Student registered successfully.");
            return "redirect:/admin/students";
        } catch (IllegalArgumentException e) {
            logger.warn("Registration failed: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("student", student);
            return "register";
        } catch (Exception e) {
            logger.error("Error registering student: {}", e.getMessage(), e);
            model.addAttribute("error", "Error occurred during registration.");
            model.addAttribute("student", student);
            return "register";
        }
    }


    @GetMapping("/students")
    public String viewStudents(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false) String keyword,
                               Model model) {
        Page<Student> studentPage;

        if (keyword != null && !keyword.isEmpty()) {
            studentPage = studentService.searchStudents(keyword, page, size);
            model.addAttribute("keyword", keyword);
        } else {
            studentPage = studentService.getPaginatedStudents(page, size);
        }

        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());

        return "admin/students";
    }

    @GetMapping("/editStudent/{studentId}")
    public String editStudent(@PathVariable Long studentId, Model model) {
        logger.info("Serving edit student form for ID: {}", studentId);
        try {
            Student student = studentService.getStudentByIdDetails(studentId);
            model.addAttribute("student", student);
            return "admin/editStudent"; // resolves to /WEB-INF/views/admin/editStudent.jsp
        } catch (IllegalArgumentException e) {
            logger.warn("Student not found: {}", studentId);
            model.addAttribute("error", "Student not found");
            model.addAttribute("students", studentService.getAllStudents()); // required for students.jsp
            return "admin/students";
        }
    }

    @PostMapping("/updateStudent/{studentId}")
    public String updateStudent(
            @PathVariable Long studentId,
            @ModelAttribute("student") Student student,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            logger.info("Updating student ID: {}", studentId);
            student.setStudentId(studentId);
            studentService.updateStudent(student, principal.getName());
            redirectAttributes.addFlashAttribute("message", "Student updated successfully.");
            return "redirect:/admin/students";
        } catch (Exception e) {
            logger.error("Failed to update student ID: {}, error={}", studentId, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Update failed: " + e.getMessage());
            return "redirect:/admin/editStudent/" + studentId;
        }
    }

    @GetMapping("/deleteStudent/{studentId}")
    public String deleteStudent(@PathVariable Long studentId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Deleting student ID: {}", studentId);
            studentService.deleteStudentById(studentId, principal.getName());
            redirectAttributes.addFlashAttribute("message", "Student deleted successfully.");
            return "redirect:/admin/students";
        } catch (Exception e) {
            logger.error("Failed to delete student ID: {}, error={}", studentId, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Deletion failed: " + e.getMessage());
            return "redirect:/admin/students";
        }
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Principal principal,
            Model model
    ) {
        logger.info("Serving admin dashboard for user: {}", principal.getName());
        int pageSize = Math.max(1, size);
        int currentPage = Math.max(1, page);
        int pageIndex = currentPage - 1;

        long studentCount = studentService.getActiveStudentsCount();
        long courseCount = courseService.getAllCourses().size();
        long marksCount = marksService.getAllMarks().size();

        Page<Student> studentPage = studentService.getStudentsPage(pageIndex, pageSize);

        model.addAttribute("username", principal.getName());
        model.addAttribute("studentList", studentPage.getContent());
        model.addAttribute("studentCount", studentCount);
        model.addAttribute("courseCount", courseCount);
        model.addAttribute("marksCount", marksCount);
        model.addAttribute("currentPage", studentPage.getNumber() + 1);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", studentPage.getTotalPages());

        return "admin/dashboard";
    }
}