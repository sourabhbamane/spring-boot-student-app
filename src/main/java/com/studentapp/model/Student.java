package com.studentapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_details")
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @NotNull(message = "DOB is required")
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @NotBlank(message = "Gender is required")
    @Column(name = "gender", nullable = false, length = 50)
    private String gender;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "created_on")
    @CreationTimestamp
    private LocalDateTime createdOn;

    @Column(name = "modified_by", length = 255)
    private String modifiedBy;

    @Column(name = "modified_on")
    @UpdateTimestamp
    private LocalDateTime modifiedOn;

    @Column(name = "delete_flag", nullable = false)
    private Boolean deleteFlag = false;
}
