package com.studentapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "student_marks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentMarks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "course_id", nullable = false)
    private Integer courseId;

    @Column(name = "marks", nullable = false)
    private Integer marks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", insertable = false, updatable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "course_id", insertable = false, updatable = false)
    private Course course;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "created_on")
    private LocalDateTime createdOn; // Remove @CreationTimestamp to handle manually

    @Column(name = "modified_by", length = 255)
    private String modifiedBy;

    @Column(name = "modified_on")
    private LocalDateTime modifiedOn; // Remove @UpdateTimestamp to handle manually

    @Column(name = "delete_flag", nullable = false)
    @Builder.Default
    private boolean deleteFlag = false;


    // Lifecycle methods to ensure proper defaults
    @PrePersist
    protected void onCreate() {
        if (createdOn == null) {
            createdOn = LocalDateTime.now();
        }
        if (modifiedOn == null) {
            modifiedOn = LocalDateTime.now();
        }
        // deleteFlag defaults to false via @Builder.Default
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedOn = LocalDateTime.now();
    }

    // Getter to use in JSP
    public String getFormattedCreatedOn() {
        return createdOn != null
                ? createdOn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                : "-";
    }
}