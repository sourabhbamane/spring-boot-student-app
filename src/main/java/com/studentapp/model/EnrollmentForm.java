package com.studentapp.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentForm {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    // Use NotEmpty (or @Size(min=1)) so an empty list fails validation
    @NotEmpty(message = "At least one course is required")
    private List<Integer> courseIds;
}