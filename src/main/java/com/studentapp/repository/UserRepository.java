package com.studentapp.repository;

import com.studentapp.enums.UserRole;
import com.studentapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndRole(String username, UserRole role);

    Optional<User> findByUsernameAndDeleteFlagFalse(String username);
    boolean existsByUsernameAndDeleteFlagFalse(String username);

}