package com.studentapp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "final_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinalReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;

    private String studentName;

    private Double averageMarks;

    private String grade;

    private Double percentile;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;
}