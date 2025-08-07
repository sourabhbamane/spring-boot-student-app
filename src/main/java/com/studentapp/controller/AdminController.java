 package com.studentapp.controller;

import com.studentapp.model.Student;
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
    public String viewAllStudents(Model model) {
        logger.info("Listing all students");
        model.addAttribute("students", studentService.getAllStudents());
        return "admin/students";
    }

    @GetMapping("/editStudent/{studentId}")
    public String editStudent(@PathVariable Long studentId, Model model) {
        logger.info("Serving edit student form for ID: {}", studentId);
        try {
            Student student = studentService.getStudentByIdDetails(studentId);
            model.addAttribute("student", student);
            return "admin/editStudent";
        } catch (IllegalArgumentException e) {
            logger.warn("Student not found: {}", studentId);
            model.addAttribute("error", "Student not found");
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









//package com.studentapp.controller;
//
//import com.studentapp.model.Course;
//import com.studentapp.model.Student;
//import com.studentapp.service.CourseService;
//import com.studentapp.service.MarksService;
//import com.studentapp.service.StudentService;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.domain.Page;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.security.Principal;
//import java.util.List;
//
//@Controller
//@RequestMapping("/admin")
//@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')") // Ensure only ADMIN can access all endpoints
//public class AdminController {
//
//    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
//
//    private final StudentService studentService;
//    private final CourseService courseService;
//    private final MarksService marksService;
//
//    // ✅ Register student - GET form
//    @GetMapping("/registerStudent")
//    public String showRegisterStudentForm(Model model) {
//        model.addAttribute("student", new Student());
//        model.addAttribute("formAction", "/admin/registerStudent");
//        return "register";
//    }
//
//    // ✅ Register student - POST form
//    @PostMapping("/registerStudent")
//    public String registerStudent(@ModelAttribute("student") Student student,
//                                  @RequestParam("userUsername") String username,
//                                  @RequestParam("userPassword") String password,
//                                  Principal principal,
//                                  Model model) {
//        try {
//            String createdBy = principal.getName(); // Logged-in user
//            studentService.saveStudentWithUser(student, username, password, createdBy);
//            return "redirect:/admin/students";
//        } catch (Exception e) {
//            logger.error("Error registering student", e);
//            model.addAttribute("error", "Error occurred during registration.");
//            return "register";
//        }
//    }
//
//    // ✅ List all students
//    @GetMapping("/students")
//    public String viewAllStudents(Model model) {
//        List<Student> students = studentService.getAllStudents();
//        model.addAttribute("students", students);
//        return "admin/students";
//    }
//
//    // ✅ Edit student - GET
//    @GetMapping("/editStudent/{id}")
//    public String editStudent(@PathVariable Long id, Model model) {
//        Student student = studentService.getStudentByIdDetails(id);
//        model.addAttribute("student", student);
//        return "admin/editStudent";
//    }
//
//    // ✅ Edit student - POST
//    @PostMapping("/updateStudent/{id}")
//    public String updateStudent(@PathVariable Long id,
//                                @ModelAttribute("student") Student student,
//                                Principal principal,
//                                RedirectAttributes redirectAttributes) {
//        student.setStudentId(id);
//        studentService.updateStudent(student, principal.getName());
//        redirectAttributes.addFlashAttribute("message", "Student updated successfully.");
//        return "redirect:/admin/students";
//    }
//
//    // ✅ Soft delete student
//    @GetMapping("/deleteStudent/{id}")
//    public String deleteStudent(@PathVariable Long id, Principal principal) {
//        studentService.deleteStudentById(id, principal.getName());
//        return "redirect:/admin/students";
//    }
//
//    // ✅ Admin dashboard
//    @GetMapping("/dashboard")
//    public String showAdminDashboard(@RequestParam(value = "page", defaultValue = "1") int page,
//                                     @RequestParam(value = "size", defaultValue = "10") int size,
//                                     Principal principal,
//                                     Model model) {
//        // normalize
//        int pageSize = Math.max(1, size);
//        int currentPage = Math.max(1, page);
//
//        long studentCount = studentService.getActiveStudentsCount();
//        long courseCount = courseService.getAllCourses().size();
//        long marksCount = marksService.getAllMarks().size();
//
//        int totalPages = (int) Math.max(1, Math.ceil((double) studentCount / pageSize));
//        currentPage = Math.min(currentPage, totalPages);
//        int pageIndex = currentPage - 1;
//
//       // List<Student> studentList = studentService.getStudentsPage(pageIndex, pageSize);
//        Page<Student> studentPage = studentService.getStudentsPage(pageIndex, pageSize);
//        List<Student> studentList = studentPage.getContent();
//
//        model.addAttribute("username", principal.getName());
//        model.addAttribute("studentList", studentList);
//        model.addAttribute("studentCount", studentCount);
//        model.addAttribute("courseCount", courseCount);
//        model.addAttribute("marksCount", marksCount);
//        model.addAttribute("currentPage", studentPage.getNumber() + 1); // 0-based → 1-based
//        model.addAttribute("pageSize", pageSize);
//        model.addAttribute("totalPages", studentPage.getTotalPages());
//
//        return "admin/dashboard";
//    }
//}