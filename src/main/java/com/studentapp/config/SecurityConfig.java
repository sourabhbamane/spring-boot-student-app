package com.studentapp.config;

import com.studentapp.security.CustomLoginSuccessHandler;
import com.studentapp.security.DbUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final CustomLoginSuccessHandler loginSuccessHandler;
    private final DbUserDetailsService dbUserDetailsService;

    public SecurityConfig(CustomLoginSuccessHandler loginSuccessHandler, DbUserDetailsService dbUserDetailsService) {
        this.loginSuccessHandler = loginSuccessHandler;
        this.dbUserDetailsService = dbUserDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        logger.debug("Configuring UserDetailsService");
        return dbUserDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(dbUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            logger.debug("Configuring ignored resources");
            web.ignoring().requestMatchers(
                    "/WEB-INF/**",
                    "/resources/**",
                    "/static/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/.well-known/**",
                    "/favicon.ico",
                    "/error"
            );
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        logger.debug("Configuring AuthenticationManager");
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.debug("Configuring SecurityFilterChain");
        http
                .authenticationProvider(authenticationProvider())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    logger.debug("Configuring request matchers");
                    auth
                            .requestMatchers(
                                    "/",
                                    "/login",
                                    "/register",
                                    "/processLogin",
                                    "/resources/**",
                                    "/static/**",
                                    "/css/**",
                                    "/js/**",
                                    "/images/**",
                                    "/.well-known/**",
                                    "/favicon.ico",
                                    "/error"
                            ).permitAll()
                            .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                            .requestMatchers("/student/**").hasAuthority("ROLE_STUDENT")
                            .anyRequest().authenticated();
                })
                .formLogin(form -> {
                    logger.debug("Configuring form login");
                    form
                            .loginPage("/login")
                            .loginProcessingUrl("/processLogin")
                            .successHandler(loginSuccessHandler)
                            .failureUrl("/login?error=true")
                            .permitAll();
                })
                .logout(logout -> {
                    logger.debug("Configuring logout");
                    logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login?logout")
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID")
                            .permitAll();
                })
                .exceptionHandling(ex -> {
                    logger.debug("Configuring exception handling");
                    ex.accessDeniedPage("/login?error=access_denied");
                })
                .sessionManagement(session -> {
                    logger.debug("Configuring session management");
                    session
                            .sessionFixation().migrateSession()
                            .maximumSessions(1)
                            .expiredUrl("/login?expired");
                });

        return http.build();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        logger.debug("Configuring HttpSessionEventPublisher");
        return new HttpSessionEventPublisher();
    }
}


//package com.studentapp.config;

//
//import com.studentapp.security.CustomLoginSuccessHandler;
//import com.studentapp.security.DbUserDetailsService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.session.HttpSessionEventPublisher;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
//
//    private final CustomLoginSuccessHandler loginSuccessHandler;
//    private final DbUserDetailsService dbUserDetailsService;
//
//    public SecurityConfig(CustomLoginSuccessHandler loginSuccessHandler, DbUserDetailsService dbUserDetailsService) {
//        this.loginSuccessHandler = loginSuccessHandler;
//        this.dbUserDetailsService = dbUserDetailsService;
//    }
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        logger.debug("Configuring UserDetailsService");
//        return dbUserDetailsService;
//    }
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> {
//            logger.debug("Configuring ignored resources");
//            web.ignoring().requestMatchers(
//                    "/WEB-INF/**",
//                    "/resources/**",
//                    "/static/**",
//                    "/css/**",
//                    "/js/**",
//                    "/images/**",
//                    "/.well-known/**",
//                    "/favicon.ico"
//            );
//        };
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        logger.debug("Configuring AuthenticationManager");
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        logger.debug("Configuring SecurityFilterChain");
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> {
//                    logger.debug("Configuring request matchers");
//                    auth
//                            .requestMatchers("/", "/login", "/register", "/processLogin", "/resources/**", "/css/**", "/js/**", "/images/**", "/.well-known/**","/favicon.ico").permitAll()
//                            .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
//                            .requestMatchers("/student/**").hasAuthority("ROLE_STUDENT")
//                            .anyRequest().authenticated();
//                })
//                .formLogin(form -> {
//                    logger.debug("Configuring form login");
//                    form
//                            .loginPage("/login")
//                            .loginProcessingUrl("/processLogin")
//                            .successHandler(loginSuccessHandler)
//                            .failureUrl("/login?error=true")
//                            .permitAll();
//                })
//                .logout(logout -> {
//                    logger.debug("Configuring logout");
//                    logout
//                            .logoutUrl("/logout")
//                            .logoutSuccessUrl("/login?logout")
//                            .invalidateHttpSession(true)
//                            .deleteCookies("JSESSIONID")
//                            .permitAll();
//                })
//                .exceptionHandling(ex -> {
//                    logger.debug("Configuring exception handling");
//                    ex.accessDeniedPage("/login?error=access_denied");
//                })
//                .sessionManagement(session -> {
//                    logger.debug("Configuring session management");
//                    session
//                            .sessionFixation().migrateSession()
//                            .maximumSessions(1)
//                            .expiredUrl("/login?expired");
//                });
//
//        return http.build();
//    }
//
//    @Bean
//    public HttpSessionEventPublisher httpSessionEventPublisher() {
//        logger.debug("Configuring HttpSessionEventPublisher");
//        return new HttpSessionEventPublisher();
//    }
//}