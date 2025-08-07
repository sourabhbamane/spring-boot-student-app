package com.studentapp.service;

import com.studentapp.enums.UserRole;
import com.studentapp.model.Student;
import com.studentapp.model.User;
import com.studentapp.repository.StudentRepository;
import com.studentapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final UserService userService;

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(UserService userService, StudentRepository studentRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    @Transactional
//    public Student saveStudent(Student student, String currentUser) {
//        if (student == null) throw new IllegalArgumentException("Student cannot be null");
//        if (!StringUtils.hasText(student.getName())) throw new IllegalArgumentException("Student name is required");
//        if (!StringUtils.hasText(student.getEmail())) throw new IllegalArgumentException("Student email is required");
//
//        String user = (currentUser != null && !currentUser.trim().isEmpty()) ? currentUser : "system";
//        Date now = new Date();
//
//        if (student.getStudentId() == null) {
//            student.setCreatedBy(user);
//            student.setCreatedOn(now);
//            student.setDeleteFlag(false);
//            logger.info("Creating student: {}", student.getEmail());
//        } else {
//            studentRepository.findById(student.getId()).ifPresent(existing -> {
//                student.setCreatedBy(existing.getCreatedBy());
//                student.setCreatedOn(existing.getCreatedOn());
//            });
//            logger.info("Updating student id={}: {}", student.getId(), student.getEmail());
//        }
//
//        student.setModifiedBy(user);
//        student.setModifiedOn(now);
//
//        return studentRepository.save(student);
//    }
//
//    @Transactional
//    public void saveStudentWithUser(Student student, String username, String password, String createdBy) {
//        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
//            throw new IllegalArgumentException("Username and password must be provided.");
//        }
//
//        // Save student
//        Student savedStudent = saveStudent(student, createdBy);
//
//        // Create linked user
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(password)); // encode password
//        user.setRole(UserRole.STUDENT);
//        user.setStudent(savedStudent);
//        user.setCreatedBy(createdBy);
//        user.setCreatedOn(new Date());
//        user.setDeleteFlag(false);
//
//        userRepository.save(user);
//
//        logger.info("Created user for student {} with username {}", savedStudent.getName(), username);
//    }

    @Transactional(readOnly = true)
    public Page<Student> getStudentsPage(int pageIndexZeroBased, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndexZeroBased, pageSize, Sort.by(Sort.Direction.DESC, "createdOn"));
        return studentRepository.findByDeleteFlagFalse(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Student> getStudentById(Long id) {
        if (id == null) return Optional.empty();
        return studentRepository.findByIdAndDeleteFlagFalse(id);
    }

    @Transactional
    public void softDeleteStudent(Long id, String currentUser) {
        studentRepository.findById(id).ifPresent(s -> {
            s.setDeleteFlag(true);
            s.setModifiedBy(currentUser == null ? "system" : currentUser);
            s.setModifiedOn(LocalDateTime.now());
            studentRepository.save(s);
            logger.info("Soft deleted student id={}", id);
        });
    }

    @Transactional(readOnly = true)
    public long getActiveStudentsCount() {
        return studentRepository.countByDeleteFlagFalse();
    }

    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findByDeleteFlagFalse();
    }

    @Transactional(readOnly = true)
    public Student getStudentByIdDetails(Long id) {
        return studentRepository.findByIdAndDeleteFlagFalse(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
    }

    @Transactional
    public void updateStudent(Student student, String modifiedBy) {
        saveStudent(student, modifiedBy); // reuse save logic
    }

    @Transactional
    public void deleteStudentById(Long id, String currentUser) {
        softDeleteStudent(id, currentUser); // reuse soft delete logic
    }


    public Student saveStudent(Student student, String createdBy) {
        logger.info("Saving student: {}", student.getName());
        student.setCreatedBy(createdBy);
        student.setModifiedBy(createdBy);
        return studentRepository.save(student);
    }

    public void saveStudentWithUser(Student student, String username, String password, String createdBy) {
        logger.info("Saving student with user: {}, username: {}", student.getName(), username);
        Student savedStudent = saveStudent(student, createdBy);
        userService.saveUserWithStudent(savedStudent, username, password, createdBy);
    }

    public Optional<Student> findById(Long id) {
        logger.debug("Finding student by ID: {}", id);
        return studentRepository.findById(id);
    }

    public List<Student> findAll() {
        logger.debug("Finding all students");
        return studentRepository.findAllByDeleteFlagFalse();
    }

    public void deleteStudent(Long id, String modifiedBy) {
        logger.info("Deleting student ID: {}", id);
        studentRepository.findById(id).ifPresent(student -> {
            student.setDeleteFlag(true);
            student.setModifiedBy(modifiedBy);
            studentRepository.save(student);
        });
    }
}