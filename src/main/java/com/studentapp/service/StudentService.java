package com.studentapp.service;

import com.studentapp.enums.UserRole;
import com.studentapp.model.Student;
import com.studentapp.model.StudentMarks;
import com.studentapp.model.User;
import com.studentapp.repository.StudentRepository;
import com.studentapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final UserQueryService userQueryService;

    private final StudentRepository studentRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(@Lazy UserQueryService userQueryService, StudentRepository studentRepository, UserService userService,
                          UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userQueryService = userQueryService;
        this.studentRepository = studentRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public Student saveStudent(Student student, String createdBy) {
        validateStudent(student); // Non-proxied method
        String user = StringUtils.hasText(createdBy) ? createdBy : "system";
        if (student.getStudentId() == null) {
            student.setCreatedBy(user);
            student.setDeleteFlag(false);
        } else {
            studentRepository.findByStudentIdAndDeleteFlagFalse(student.getStudentId()).ifPresent(existing -> {
                student.setCreatedBy(existing.getCreatedBy());
                student.setCreatedOn(existing.getCreatedOn());
            });
        }
        student.setModifiedBy(user);
        return studentRepository.save(student);
    }

    private void validateStudent(Student student) {
        if (student == null) throw new IllegalArgumentException("Student cannot be null");
        if (!StringUtils.hasText(student.getName())) throw new IllegalArgumentException("Student name is required");
        if (!StringUtils.hasText(student.getEmail())) throw new IllegalArgumentException("Student email is required");
    }
//    @Transactional
//    public Student saveStudent(Student student, String createdBy) {
//        if (student == null) throw new IllegalArgumentException("Student cannot be null");
//        if (!StringUtils.hasText(student.getName())) throw new IllegalArgumentException("Student name is required");
//        if (!StringUtils.hasText(student.getEmail())) throw new IllegalArgumentException("Student email is required");
//
//        String user = StringUtils.hasText(createdBy) ? createdBy : "system";
//        logger.info("Saving student: {}", student.getName());
//
//        if (student.getStudentId() == null) {
//            student.setCreatedBy(user);
//            student.setDeleteFlag(false);
//        } else {
//            studentRepository.findByStudentIdAndDeleteFlagFalse(student.getStudentId()).ifPresent(existing -> {
//                student.setCreatedBy(existing.getCreatedBy());
//                student.setCreatedOn(existing.getCreatedOn());
//            });
//            logger.info("Updating student ID: {}", student.getStudentId());
//        }
//        student.setModifiedBy(user);
//        return studentRepository.save(student);
//    }

//    @Transactional
//    public void saveStudentWithUser(Student student, String username, String password, String createdBy) {
//        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
//            throw new IllegalArgumentException("Username and password must be provided");
//        }
//        if (userQueryService.usernameExists(username.trim())) {
//            throw new IllegalArgumentException("Username already exists: " + username);
//        }
//
//        logger.info("Saving student with user: {}, username: {}", student.getName(), username);
//        Student savedStudent = saveStudent(student, createdBy);
//
//        User user = new User();
//        user.setUsername(username.trim());
//        user.setPassword(passwordEncoder.encode(password));
//        user.setRole(UserRole.STUDENT);
//        //user.setStudent(savedStudent);
//        user.setCreatedBy(createdBy);
//        user.setDeleteFlag(false);
//        userRepository.save(user);
//        logger.info("Created user for student {} with username {}", savedStudent.getName(), username);
//    }

    @Transactional
    public void saveStudentWithUser(Student student, String username, String password, String createdBy) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new IllegalArgumentException("Username and password must be provided");
        }
        if (userService.usernameExists(username.trim())) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        logger.info("Saving student with user: {}, username: {}", student.getName(), username);
        Student savedStudent = saveStudent(student, createdBy);

        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(UserRole.STUDENT);
        user.setStudentId(savedStudent.getStudentId()); // Set student_id
        user.setCreatedBy(createdBy);
        user.setDeleteFlag(false);
        userRepository.save(user);
        logger.info("Created user for student {} with username {}", savedStudent.getName(), username);
    }
    public List<Student> getAllStudents() {
        logger.debug("Finding all students");
        return studentRepository.findAllByDeleteFlagFalse();
    }

    public Student getStudentByIdDetails(Long studentId) {
        logger.debug("Fetching student details by ID: {}", studentId);
        return studentRepository.findByStudentIdAndDeleteFlagFalse(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
    }

    @Transactional
    public void updateStudent(Student student, String modifiedBy) {
        logger.info("Updating student ID: {}", student.getStudentId());
        saveStudent(student, modifiedBy);
    }

    @Transactional
    public void deleteStudentById(Long studentId, String modifiedBy) {
        logger.info("Deleting student ID: {}", studentId);
        studentRepository.findByStudentIdAndDeleteFlagFalse(studentId).ifPresent(student -> {
            student.setDeleteFlag(true);
            student.setModifiedBy(StringUtils.hasText(modifiedBy) ? modifiedBy : "system");
            studentRepository.save(student);
        });
    }

    public Page<Student> getStudentsPage(int pageIndexZeroBased, int pageSize) {
        logger.debug("Fetching students page: {}, size: {}", pageIndexZeroBased, pageSize);
        Pageable pageable = PageRequest.of(pageIndexZeroBased, pageSize, Sort.by(Sort.Direction.DESC, "createdOn"));
        return studentRepository.findByDeleteFlagFalse(pageable);
    }

    public long getActiveStudentsCount() {
        logger.debug("Counting active students");
        return studentRepository.countByDeleteFlagFalse();
    }

    public Optional<Student> findByEmail(String email) {
        logger.debug("Finding student by email: {}", email);
        return studentRepository.findByEmail(email);
    }
    @Transactional(readOnly = true)
    public Optional<Student> getStudentById(Long id) {
        if (id == null) return Optional.empty();
        return studentRepository.findByStudentIdAndDeleteFlagFalse(id);
    }

    public Page<Student> searchStudents(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return studentRepository.findByNameContainingIgnoreCaseAndDeleteFlagFalse(keyword, pageable);
    }
    public Page<Student> getPaginatedStudents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return studentRepository.findByDeleteFlagFalse(pageable);
    }
}