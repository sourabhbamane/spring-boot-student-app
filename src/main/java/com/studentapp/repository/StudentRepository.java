package com.studentapp.repository;

import com.studentapp.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // find active (not soft-deleted)
    List<Student> findByDeleteFlagFalse();
    Optional<Student> findByIdAndDeleteFlagFalse(Long studentId);


    // count active
    long countByDeleteFlagFalse();

    // custom page with native query if you prefer; otherwise use Pageable in service:
     Page<Student> findByDeleteFlagFalse(Pageable pageable);

    // find by email
    Optional<Student> findByEmail(String email);
    List<Student> findAllByDeleteFlagFalse();
}