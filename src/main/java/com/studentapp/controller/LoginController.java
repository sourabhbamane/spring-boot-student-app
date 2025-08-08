package com.studentapp.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String showLoginPage(
            HttpSession session,
            Model model,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired
    ) {
        logger.info("Serving login page, session ID: {}, username: {}",
                session.getId(),
                SecurityContextHolder.getContext().getAuthentication() != null ?
                        SecurityContextHolder.getContext().getAuthentication().getName() : "anonymous");

        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            String role = (String) session.getAttribute("role");
            logger.info("User already authenticated with role: {}, redirecting", role);
            if ("ADMIN".equals(role)) {
                return "redirect:/admin/dashboard";
            } else if ("STUDENT".equals(role)) {
                return "redirect:/student/dashboard";
            } else {
                logger.warn("Unknown role: {}, redirecting to login with error", role);
                return "redirect:/login?error=access_denied";
            }
        }

        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }
        if (expired != null) {
            model.addAttribute("error", "Session expired, please log in again");
        }

        logger.debug("No authenticated user, rendering login page");
        return "login";
    }
}



// package com.studentapp.controller;
//
//import jakarta.servlet.http.HttpSession;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class LoginController {
//
//    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
//
//    @GetMapping("/login")
//    public String loginPage() {
//        return "login";
//    }
//
////    @GetMapping("/login")
////    public String showLoginPage(HttpSession session, Model model) {
////        logger.info("Serving login page, session ID: {}, username: {}",
////                session.getId(),
////                SecurityContextHolder.getContext().getAuthentication() != null ?
////                        SecurityContextHolder.getContext().getAuthentication().getName() : "anonymous");
////
////        if (SecurityContextHolder.getContext().getAuthentication() != null &&
////                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
////                !"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
////            String role = (String) session.getAttribute("role");
////            logger.info("User already authenticated with role: {}, redirecting", role);
////            if ("ADMIN".equals(role)) {
////                return "redirect:/admin/dashboard";
////            } else if ("STUDENT".equals(role)) {
////                return "redirect:/student/dashboard";
////            } else {
////                logger.warn("Unknown role: {}, redirecting to login with error", role);
////                return "redirect:/login?error=access_denied";
////            }
////        }
////        logger.debug("No authenticated user, rendering login page");
////        return "login";
////    }
//}