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

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Save a new user or update existing.
     * Encodes the password if it looks like raw text (not starting with BCrypt prefix).
     * Returns the saved User.
     */
    @Transactional
    public User saveUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }

        // encode password if needed
        String pwd = user.getPassword();
        if (pwd != null && !pwd.startsWith("$2a$") && !pwd.startsWith("$2b$") && !pwd.startsWith("$2y$")) {
            user.setPassword(passwordEncoder.encode(pwd));
        }

        try {
            User saved = userRepository.save(user);
            logger.info("Saved user: {}", saved.getUsername());
            return saved;
        } catch (DataIntegrityViolationException e) {
            // Likely duplicate username (unique constraint) — wrap for clearer message
            logger.error("Failed to save user (possible duplicate): {}", user.getUsername(), e);
            throw new IllegalStateException("Failed to save user: username might already exist", e);
        }
    }

//    @Transactional(readOnly = true)
//    public Optional<User> findByUsername(String username) {
//        if (username == null) return Optional.empty();
//        return userRepository.findByUsername(username.trim());
//    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsernameAndRole(String username, UserRole role) {
        if (username == null || role == null) return Optional.empty();
        return userRepository.findByUsernameAndRole(username.trim(), role);
    }

    /**
     * Authenticate user — returns Optional<User> if username+role found and password matches.
     */
    @Transactional(readOnly = true)
    public Optional<User> authenticateUser(String username, String rawPassword, String role) {
        if (username == null || rawPassword == null || role == null) {
            logger.warn("Authentication attempt with nulls - username: {}, role: {}", username, role);
            return Optional.empty();
        }

        UserRole userRole;
        try {
            userRole = UserRole.valueOf(role.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid role during authentication: {}", role);
            return Optional.empty();
        }

        Optional<User> userOpt = userRepository.findByUsernameAndRole(username.trim(), userRole);
        if (userOpt.isEmpty()) {
            logger.warn("No user found with username={} role={}", username, userRole);
            return Optional.empty();
        }

        User user = userOpt.get();
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            logger.info("Authentication successful for user {}", username);
            return Optional.of(user);
        } else {
            logger.warn("Authentication failed for user {}", username);
            return Optional.empty();
        }
    }

//    @Transactional(readOnly = true)
//    public boolean usernameExists(String username) {
//        return username != null && userRepository.findByUsername(username.trim()).isPresent();
//    }
//
//    public String encodePassword(String rawPassword) {
//        return passwordEncoder.encode(rawPassword);
//    }


    @Transactional(readOnly = true)
    public Optional<Student> getStudentByUsername(String username) {
        return findByUsername(username).map(User::getStudent);
    }


    public Optional<User> findByUsername(String username) {
        logger.debug("Finding user by username: {}", username);
        return userRepository.findByUsernameAndDeleteFlagFalse(username);
    }

    public boolean usernameExists(String username) {
        logger.debug("Checking if username exists: {}", username);
        return userRepository.existsByUsernameAndDeleteFlagFalse(username);
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public User saveUserWithStudent(Student student, String username, String rawPassword, String createdBy) {
        logger.info("Saving user for student: {}, username: {}", student.getName(), username);
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(UserRole.STUDENT);
        user.setStudentId(student.getStudentId());
        user.setCreatedBy(createdBy);
        user.setModifiedBy(createdBy);
        return userRepository.save(user);
    }

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
}