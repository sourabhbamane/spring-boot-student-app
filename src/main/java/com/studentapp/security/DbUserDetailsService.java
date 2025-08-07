package com.studentapp.security;

import com.studentapp.model.User;
import com.studentapp.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DbUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DbUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Map role enum to authority string, e.g. "ROLE_ADMIN" or "ROLE_STUDENT"
        String roleName = u.getRole() != null ? "ROLE_" + u.getRole().name() : "ROLE_USER";
        List<SimpleGrantedAuthority> auths = List.of(new SimpleGrantedAuthority(roleName));

        return org.springframework.security.core.userdetails.User.builder()
                .username(u.getUsername())
                .password(u.getPassword()) // must be BCrypt hashed when saved
                .authorities(auths)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}