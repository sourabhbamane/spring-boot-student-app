package com.studentapp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
}