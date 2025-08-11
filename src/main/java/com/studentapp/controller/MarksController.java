package com.studentapp.controller;

import com.studentapp.dto.MarksDTO;
import com.studentapp.kafka.KafkaProducer;
import com.studentapp.kafka.MarksEvent;
import com.studentapp.model.*;
import com.studentapp.service.CourseService;
import com.studentapp.service.MarksService;
import com.studentapp.service.StudentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/admin/marks")
@PreAuthorize("hasRole('ADMIN')")
public class MarksController {

    private static final Logger logger = LoggerFactory.getLogger(MarksController.class);

    private final MarksService marksService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final KafkaProducer kafkaProducer;

    public MarksController(MarksService marksService,
                           StudentService studentService,
                           CourseService courseService,
                           KafkaProducer kafkaProducer) {
        this.marksService = marksService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/assign")
    public String showAssignForm(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", List.of()); // AJAX loaded
        model.addAttribute("marksDTO", new MarksDTO());
        return "admin/assignMarks";
    }

    @PostMapping("/save")
    public String saveMarks(
            @ModelAttribute("marksDTO") @Valid MarksDTO marksDTO,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            // Handle validation errors based on whether this is edit or create
            if (marksDTO.getId() != null) {
                // This is an edit operation - populate names for redisplay
                populateStudentAndCourseNames(marksDTO);
                model.addAttribute("error", "Please correct the form errors.");
                return "admin/editMarks";
            } else {
                model.addAttribute("error", "Please correct the form errors.");
                model.addAttribute("students", studentService.getAllStudents());
                model.addAttribute("courses", List.of());
                return "admin/assignMarks";
            }
        }

        Optional<Student> studentOpt = studentService.getStudentById(marksDTO.getStudentId());
        Optional<Course> courseOpt = courseService.getCourseById(marksDTO.getCourseId());

        if (studentOpt.isEmpty() || courseOpt.isEmpty()) {
            handleError(marksDTO, model, "Invalid student or course.");
            return getReturnView(marksDTO);
        }

        if (!courseService.isStudentEnrolled(marksDTO.getStudentId(), marksDTO.getCourseId())) {
            handleError(marksDTO, model, "Student is not enrolled in the selected course.");
            return getReturnView(marksDTO);
        }

        // Check for existing marks (but exclude current record if editing)
        Optional<StudentMarks> existingOpt =
                marksService.getMarksByStudentIdAndCourseId(marksDTO.getStudentId(), marksDTO.getCourseId());

        if (existingOpt.isPresent() &&
                !Objects.equals(existingOpt.get().getId(), marksDTO.getId()) &&
                !existingOpt.get().isDeleteFlag()) {
            handleError(marksDTO, model, "Marks already assigned for this student and course.");
            return getReturnView(marksDTO);
        }

        // Save the marks
        StudentMarks studentMarks = StudentMarks.builder()
                .id(marksDTO.getId()) // Will be null for new records, not null for updates
                .studentId(marksDTO.getStudentId())
                .courseId(marksDTO.getCourseId())
                .marks(marksDTO.getMarks())
                .build();

        String currentUser = userDetails != null ? userDetails.getUsername() : "system";
        marksService.saveOrUpdateMarks(studentMarks, currentUser);

        // Build event - use correct logic to determine action
        String action = (marksDTO.getId() != null) ? "UPDATE" : "CREATE";
        MarksEvent event = MarksEvent.builder()
                .studentId(marksDTO.getStudentId())
                .courseId(Long.valueOf(marksDTO.getCourseId()))
                .marks(marksDTO.getMarks())
                .action(action)
                .timestamp(LocalDateTime.now())
                .build();

        // Send to Kafka
        kafkaProducer.sendMarksEvent(event);
        kafkaProducer.sendStudentGradeRecalcTrigger(marksDTO.getStudentId());

        redirectAttributes.addFlashAttribute("success",
                action.equals("UPDATE") ? "Marks updated successfully!" : "Marks assigned successfully!");

        return "redirect:/admin/marks/list";
    }

    private void handleError(MarksDTO marksDTO, Model model, String errorMessage) {
        model.addAttribute("error", errorMessage);
        if (marksDTO.getId() != null) {
            // Edit mode - populate student and course names
            populateStudentAndCourseNames(marksDTO);
        } else {
            // Create mode - populate dropdowns
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("courses", List.of());
        }
    }

    private String getReturnView(MarksDTO marksDTO) {
        return (marksDTO.getId() != null) ? "admin/editMarks" : "admin/assignMarks";
    }

    private void populateStudentAndCourseNames(MarksDTO marksDTO) {
        Optional<Student> studentOpt = studentService.getStudentById(marksDTO.getStudentId());
        Optional<Course> courseOpt = courseService.getCourseById(marksDTO.getCourseId());

        if (studentOpt.isPresent()) {
            marksDTO.setStudentName(studentOpt.get().getName());
        }
        if (courseOpt.isPresent()) {
            marksDTO.setCourseName(courseOpt.get().getCourseName());
        }
    }

    @GetMapping("/list")
    public String viewAllMarks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Page<StudentMarks> marksPage = marksService.getMarksPage(page, size);
        model.addAttribute("marksPage", marksPage);
        model.addAttribute("currentPage", page);
        return "admin/marksList";
    }

//    @GetMapping("/edit/{id}")
//    public String editMarks(@PathVariable("id") long id, Model model) {
//        Optional<StudentMarks> existing = marksService.getMarksById(id);
//        if (existing.isEmpty()) {
//            model.addAttribute("error", "Marks not found.");
//            return "redirect:/admin/marks/list";
//        }
//
//        StudentMarks sm = existing.get();
//        MarksDTO marksDTO = new MarksDTO(sm.getId(), sm.getStudentId(), sm.getCourseId(), sm.getMarks());
//
//        model.addAttribute("marksDTO", marksDTO);
//        model.addAttribute("students", studentService.getAllStudents());
//        model.addAttribute("courses", List.of());
//        return "admin/assignMarks";
//    }
@GetMapping("/edit/{id}")
public String editMarks(@PathVariable("id") long id, Model model) {
    Optional<StudentMarks> existing = marksService.getMarksById(id);
    if (existing.isEmpty()) {
        model.addAttribute("error", "Marks not found.");
        return "redirect:/admin/marks/list";
    }

    StudentMarks sm = existing.get();

    // Get student and course details for display
    Optional<Student> studentOpt = studentService.getStudentById(sm.getStudentId());
    Optional<Course> courseOpt = courseService.getCourseById(sm.getCourseId());

    if (studentOpt.isEmpty() || courseOpt.isEmpty()) {
        model.addAttribute("error", "Student or course not found.");
        return "redirect:/admin/marks/list";
    }

    Student student = studentOpt.get();
    Course course = courseOpt.get();

    // Create MarksDTO with all required fields including names
    MarksDTO marksDTO = MarksDTO.builder()
            .id(sm.getId())
            .studentId(sm.getStudentId())
            .courseId(sm.getCourseId())
            .marks(sm.getMarks())
            .studentName(student.getName()) // Add student name
            .courseName(course.getCourseName())   // Add course name
            .build();

    model.addAttribute("marksDTO", marksDTO);
    return "admin/editMarks";
}

    @GetMapping("/delete/{id}")
    public String deleteMarks(@PathVariable("id") long id,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        String currentUser = userDetails != null ? userDetails.getUsername() : "system";
        marksService.deleteMarks(id, currentUser);
        redirectAttributes.addFlashAttribute("success", "Marks deleted successfully!");
        return "redirect:/admin/marks/list";
    }

    @GetMapping(value = "/courses/enrolled/{studentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<CourseDTO>> getEnrolledCourses(@PathVariable Long studentId) {
        List<CourseDTO> enrolledCourses = courseService.getEnrolledCourses(studentId);
        return ResponseEntity.ok(enrolledCourses != null ? enrolledCourses : List.of());
    }
}