package com.studentapp.model;

import com.studentapp.enums.UserRole;
import com.studentapp.model.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_new")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 80)
    private String username;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Column(name = "role", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "student_id")
    private Long studentId;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", insertable = false, updatable = false)
    private Student student;

    public boolean isStudentRole() {
        return UserRole.STUDENT.equals(this.role);
    }
}
