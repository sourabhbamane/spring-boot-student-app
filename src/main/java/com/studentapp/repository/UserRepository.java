package com.studentapp.repository;

import com.studentapp.enums.UserRole;
import com.studentapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Optional<User> findByUsername(String username);
//    Optional<User> findByUsernameAndRole(String username, UserRole role);
//
//    Optional<User> findByUsernameAndDeleteFlagFalse(String username);
//    boolean existsByUsernameAndDeleteFlagFalse(String username);

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.deleteFlag = false")
    Optional<User> findByUsernameAndDeleteFlagFalse(String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username AND u.deleteFlag = false")
    boolean existsByUsernameAndDeleteFlagFalse(String username);

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.role = :role")
    Optional<User> findByUsernameAndRole(String username, UserRole role);
}