package com.studentapp.security;

import com.studentapp.enums.UserRole;
import com.studentapp.model.User;
import com.studentapp.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Lazy
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        String username = authentication.getName();
        session.setAttribute("username", username);

        Optional<User> optionalUser = userService.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            session.setAttribute("role", user.getRole() != null ? user.getRole().name() : "UNKNOWN");
            if (user.isStudentRole() && user.getStudent() != null) {
                session.setAttribute("studentId", user.getStudent().getStudentId());
            }
        } else {
            session.setAttribute("role", "UNKNOWN");
        }

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT"))) {
            response.sendRedirect(request.getContextPath() + "/student/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/login?error=access_denied");
        }
    }
}
