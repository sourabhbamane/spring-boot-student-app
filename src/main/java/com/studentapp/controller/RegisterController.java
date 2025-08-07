package com.studentapp.controller;

import com.studentapp.enums.UserRole;
import com.studentapp.model.Student;
import com.studentapp.model.User;
import com.studentapp.service.StudentService;
import com.studentapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

@Controller
public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    private final StudentService studentService;
    private final UserService userService;

    public RegisterController(StudentService studentService, UserService userService) {
        this.studentService = studentService;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        logger.info("Serving register page.");
        model.addAttribute("student", new Student());
        return "register";
    }

    @PostMapping("/register")
    public String registerStudent(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("dob") String dob,
            @RequestParam("gender") String gender,
            @RequestParam("address") String address,
            @RequestParam("userUsername") String username,
            @RequestParam("userPassword") String password,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            if (userService.usernameExists(username.trim())) {
                logger.warn("Username already exists: {}", username);
                model.addAttribute("error", "Username already exists.");
                model.addAttribute("student", new Student());
                return "register";
            }

           // Date parsedDob = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
            Student student = new Student();
            student.setName(name.trim());
            student.setEmail(email.trim());
            student.setDob(LocalDate.parse(dob));
            student.setGender(gender.trim());
            student.setAddress(address.trim());

            studentService.saveStudentWithUser(student, username.trim(), password, "self-register");

            logger.info("Student registered successfully: {}", username);
            redirectAttributes.addFlashAttribute("message", "Registration successful! Please log in.");
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Failed to register student: {}, error={}", username, e.getMessage(), e);
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            model.addAttribute("student", new Student());
            return "register";
        }
    }

    @GetMapping("/api/checkUsername")
    @ResponseBody
    public Map<String, Boolean> checkUsername(@RequestParam String username) {
        boolean exists = userService.usernameExists(username.trim());
        return Map.of("exists", exists);
    }
}
