package com.studentapp.kafka;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarksEvent {
    private Long studentId;
    private Long courseId;
    private Integer marks;
    private String action; // e.g., "CREATE", "UPDATE"
    private LocalDateTime timestamp;
}