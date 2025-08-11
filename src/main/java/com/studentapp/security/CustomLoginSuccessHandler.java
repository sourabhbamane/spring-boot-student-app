package com.studentapp.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomLoginSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        logger.debug("Processing successful authentication for user: {}", authentication.getName());
        HttpSession session = request.getSession();
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse("UNKNOWN");

        logger.info("Successful login for user: {}, role: {}", username, role);
        session.setAttribute("username", username);
        session.setAttribute("role", role);

        String redirectUrl = "ADMIN".equals(role) ? "/admin/dashboard" : "/student/dashboard";
        logger.debug("Redirecting to: {}", redirectUrl);
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}


//package com.studentapp.security;
//
//import com.studentapp.model.Student;
//import com.studentapp.model.User;
//import com.studentapp.repository.UserRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Optional;
//
//@Component
//public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
//
//    private static final Logger logger = LoggerFactory.getLogger(CustomLoginSuccessHandler.class);
//
//    private final UserRepository userRepository;
//
//    public CustomLoginSuccessHandler(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException {
//        HttpSession session = request.getSession();
//        String username = authentication.getName();
//        String role = authentication.getAuthorities().stream()
//                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
//                .findFirst()
//                .orElse("UNKNOWN");
//
//        logger.info("Successful login for user: {}, role: {}", username, role);
//        session.setAttribute("username", username);
//        session.setAttribute("role", role);
//
//        if ("STUDENT".equals(role)) {
//            Optional<User> userOpt = userRepository.findByUsernameAndDeleteFlagFalse(username);
//            if (userOpt.isPresent()) {
//                Student student = userOpt.get().getStudent();
//                if (student != null) {
//                    session.setAttribute("studentId", student.getStudentId());
//                    logger.debug("Set session studentId: {}", student.getStudentId());
//                }
//            }
//        }
//
//        String redirectUrl = "ADMIN".equals(role) ? "/admin/dashboard" : "/student/dashboard";
//        response.sendRedirect(request.getContextPath() + redirectUrl);
//    }
//}