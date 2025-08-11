package com.studentapp.service;

import com.studentapp.enums.UserRole;
import com.studentapp.model.Student;
import com.studentapp.model.User;
import com.studentapp.repository.StudentRepository;
import com.studentapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserQueryService {

    private static final Logger logger = LoggerFactory.getLogger(UserQueryService.class);

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserQueryService(UserRepository userRepository, StudentRepository studentRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    public Optional<User> findByUsername(String username) {
//        logger.debug("Finding user by username: {}", username);
//        return userRepository.findByUsernameAndDeleteFlagFalse(username);
//    }

    public Optional<User> findByUsernameAndRole(String username, UserRole role) {
        if (username == null || role == null) {
            logger.warn("Null username or role");
            return Optional.empty();
        }
        logger.debug("Finding user by username: {} and role: {}", username, role);
        return userRepository.findByUsernameAndRole(username.trim(), role);
    }

//    public boolean usernameExists(String username) {
//        if (username == null) {
//            logger.warn("Null username");
//            return false;
//        }
//        logger.debug("Checking if username exists: {}", username);
//        return userRepository.existsByUsernameAndDeleteFlagFalse(username.trim());
//    }

    public Optional<User> authenticateUser(String username, String rawPassword, String role) {
        if (username == null || rawPassword == null || role == null) {
            logger.warn("Null parameters in authentication");
            return Optional.empty();
        }

        UserRole userRole;
        try {
            userRole = UserRole.valueOf(role.trim().toUpperCase());
            logger.debug("Parsed role: {}", userRole);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid role: {}", role);
            return Optional.empty();
        }

        Optional<User> userOpt = userRepository.findByUsernameAndRole(username.trim(), userRole);
        if (userOpt.isEmpty()) {
            logger.warn("No user found with username={} and role={}", username, userRole);
            return Optional.empty();
        }

        User user = userOpt.get();
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            logger.debug("Password matched for user: {}", username);
            return Optional.of(user);
        } else {
            logger.warn("Incorrect password for user {}", username);
            return Optional.empty();
        }
    }


    public Optional<Student> getStudentByUsername(String username) {
        logger.debug("Getting student by username: {}", username);
        return userRepository.findByUsernameAndDeleteFlagFalse(username)
                .filter(user -> user.getStudentId() != null)
                .flatMap(user -> studentRepository.findByStudentIdAndDeleteFlagFalse(user.getStudentId()));
    }
}