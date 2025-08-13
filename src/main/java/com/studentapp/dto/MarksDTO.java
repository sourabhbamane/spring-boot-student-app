package com.studentapp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarksDTO {

    private Long id;

    @NotNull(message = "Student must be selected")
    private Long studentId;

    @NotNull(message = "Course must be selected")
    private Integer courseId;

    @NotNull(message = "Marks are required")
    @Min(value = 0, message = "Marks must be >= 0")
    @Max(value = 100, message = "Marks must be <= 100")
    private Integer marks;

    // Additional properties for display purposes
    private String studentName;
    private String courseName;
    private String createdBy;
    private LocalDateTime createdOn;
    // Constructor for backwards compatibility (if needed elsewhere)
    public MarksDTO(Long id, Long studentId, Integer courseId, Integer marks) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.marks = marks;
    }

    // Getter to use in JSP
    public String getFormattedCreatedOn() {
        return createdOn != null
                ? createdOn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                : "-";
    }
}