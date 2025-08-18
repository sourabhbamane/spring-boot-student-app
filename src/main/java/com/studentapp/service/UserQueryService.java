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

    public UserQueryService(UserRepository userRepository, StudentRepository studentRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }


    public Optional<Student> getStudentByUsername(String username) {
        logger.debug("Getting student by username: {}", username);
        return userRepository.findByUsernameAndDeleteFlagFalse(username)
                .filter(user -> user.getStudentId() != null)
                .flatMap(user -> studentRepository.findByStudentIdAndDeleteFlagFalse(user.getStudentId()));
    }
}