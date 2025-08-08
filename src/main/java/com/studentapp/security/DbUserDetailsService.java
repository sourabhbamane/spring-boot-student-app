package com.studentapp.security;

import com.studentapp.model.User;
import com.studentapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class DbUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(DbUserDetailsService.class);

    private final UserRepository userRepository;

    public DbUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("=== Starting loadUserByUsername for: {} ===", username);

        if (username == null || username.trim().isEmpty()) {
            logger.error("Username is null or empty");
            throw new UsernameNotFoundException("Username cannot be null or empty");
        }

        try {
            logger.debug("Querying database for user: {}", username);
            Optional<User> userOpt = userRepository.findByUsernameAndDeleteFlagFalse(username);

            if (userOpt.isEmpty()) {
                logger.warn("User not found in database: {}", username);
                throw new UsernameNotFoundException("User not found: " + username);
            }

            User user = userOpt.get();
            logger.debug("Database query successful - User found: {}", user.getUsername());
            logger.debug("User role from database: {}", user.getRole());
            logger.debug("User password (length): {}", user.getPassword() != null ? user.getPassword().length() : "null");

            if (user.getRole() == null) {
                logger.error("User role is null for user: {}", username);
                throw new UsernameNotFoundException("User role is null for user: " + username);
            }

            String role = "ROLE_" + user.getRole().name();
            logger.debug("Constructed role: {}", role);

            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(role))
            );

            logger.debug("=== Successfully created UserDetails for: {} ===", username);
            return userDetails;

        } catch (UsernameNotFoundException e) {
            logger.error("UsernameNotFoundException for user: {}", username, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error loading user: {}", username, e);
            throw new UsernameNotFoundException("Error loading user: " + username, e);
        }
    }
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        logger.debug("Attempting to load user by username: {}", username);
//        try {
//            Optional<User> userOpt = userRepository.findByUsernameAndDeleteFlagFalse(username);
//            if (userOpt.isEmpty()) {
//                logger.warn("User not found: {}", username);
//                throw new UsernameNotFoundException("User not found: " + username);
//            }
//            User user = userOpt.get();
//            logger.debug("Found user: {}, role: {}, student_id: {}", user.getUsername(), user.getRole(), user.getStudentId());
//            String role = "ROLE_" + user.getRole().name();
//            logger.debug("Assigning role: {}", role);
//            return new org.springframework.security.core.userdetails.User(
//                    user.getUsername(),
//                    user.getPassword(),
//                    Collections.singletonList(new SimpleGrantedAuthority(role))
//            );
//        } catch (Exception e) {
//            logger.error("Error loading user: {}", username, e);
//            throw new UsernameNotFoundException("Error loading user: " + username, e);
//        }
//    }
}