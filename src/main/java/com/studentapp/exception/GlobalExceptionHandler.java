package com.studentapp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleBadRequest(IllegalArgumentException ex, RedirectAttributes ra) {
        logger.warn("Bad request: {}", ex.getMessage());
        ra.addFlashAttribute("error", ex.getMessage());
        return "redirect:/admin/dashboard"; // or back to the form url (you might customize per-controller)
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrity(DataIntegrityViolationException ex, RedirectAttributes ra) {
        logger.error("Data integrity issue", ex);
        ra.addFlashAttribute("error", "Database constraint violation: " + ex.getMostSpecificCause().getMessage());
        return "redirect:/admin/dashboard";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        logger.warn("Validation failed: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public String handleAll(Exception ex, RedirectAttributes ra) {
        logger.error("Unexpected error", ex);
        ra.addFlashAttribute("error", "An unexpected error occurred. Please try again.");
        return "redirect:/error"; // put a friendly error page or dashboard
    }
}