package com.studentapp.service;

import com.studentapp.enums.UserRole;
import com.studentapp.model.Student;
import com.studentapp.model.User;
import com.studentapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public boolean usernameExists(String username) {
        return userRepository.existsByUsernameAndDeleteFlagFalse(username.trim());
    }
    @Transactional
    public User saveUser(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username and user must not be null");
        }

        String pwd = user.getPassword();
        if (pwd != null && !pwd.startsWith("$2a$") && !pwd.startsWith("$2b$") && !pwd.startsWith("$2y$")) {
            user.setPassword(passwordEncoder.encode(pwd));
        }

        try {
            User saved = userRepository.save(user);
            logger.info("Saved user: {}", saved.getUsername());
            return saved;
        } catch (DataIntegrityViolationException e) {
            logger.error("Duplicate username: {}", user.getUsername(), e);
            throw new IllegalStateException("Failed to save user: username might already exist", e);
        }
    }

//    @Transactional
//    public User saveUserWithStudent(Student student, String username, String rawPassword, String createdBy) {
//        logger.info("Saving user for student: {}, username: {}", student.getName(), username);
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(rawPassword));
//        user.setRole(UserRole.STUDENT);
//        user.setStudent(student);
//        user.setStudentId(student.getStudentId());
//        user.setCreatedBy(createdBy);
//        user.setModifiedBy(createdBy);
//        return userRepository.save(user);
//    }

    @Transactional
    public User saveAdminUser(String username, String rawPassword, String createdBy) {
        logger.info("Saving admin user: {}", username);
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(UserRole.ADMIN);
        user.setCreatedBy(createdBy);
        user.setModifiedBy(createdBy);
        return userRepository.save(user);
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}



//package com.studentapp.service;
//
//import com.studentapp.enums.UserRole;
//import com.studentapp.model.Student;
//import com.studentapp.model.User;
//import com.studentapp.repository.UserRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class UserService {
//
//    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
//
//    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder passwordEncoder;
//
//    public UserService(UserRepository userRepository,
//                       BCryptPasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Transactional
//    public User saveUser(User user) {
//        if (user == null || user.getUsername() == null || user.getUsername().trim().isEmpty()) {
//            throw new IllegalArgumentException("Username and user must not be null");
//        }
//
//        String pwd = user.getPassword();
//        if (pwd != null && !pwd.startsWith("$2a$") && !pwd.startsWith("$2b$") && !pwd.startsWith("$2y$")) {
//            user.setPassword(passwordEncoder.encode(pwd));
//        }
//
//        try {
//            User saved = userRepository.save(user);
//            logger.info("Saved user: {}", saved.getUsername());
//            return saved;
//        } catch (DataIntegrityViolationException e) {
//            logger.error("Duplicate username: {}", user.getUsername(), e);
//            throw new IllegalStateException("Failed to save user: username might already exist", e);
//        }
//    }
//
//    public User saveUserWithStudent(Student student, String username, String rawPassword, String createdBy) {
//        logger.info("Saving user for student: {}, username: {}", student.getName(), username);
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(rawPassword));
//        user.setRole(UserRole.STUDENT);
//        user.setStudentId(student.getStudentId());
//        user.setCreatedBy(createdBy);
//        user.setModifiedBy(createdBy);
//        return userRepository.save(user);
//    }
//
//    public User saveAdminUser(String username, String rawPassword, String createdBy) {
//        logger.info("Saving admin user: {}", username);
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(rawPassword));
//        user.setRole(UserRole.ADMIN);
//        user.setCreatedBy(createdBy);
//        user.setModifiedBy(createdBy);
//        return userRepository.save(user);
//    }
//
//    public String encodePassword(String rawPassword) {
//        return passwordEncoder.encode(rawPassword);
//    }
//}