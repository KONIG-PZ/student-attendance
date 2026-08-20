package student_attendance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import student_attendance.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // ============================
    // SECURITY FILTER CHAIN
    // ============================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http){

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Public login
                        .requestMatchers("/api/auth/**")
                        .permitAll()

                        // Teacher + Super Admin
                        .requestMatchers("/api/students/**")
                        .hasAnyRole("TEACHER", "SUPER_ADMIN")

                        .requestMatchers("/api/attendance/**")
                        .hasAnyRole("TEACHER", "SUPER_ADMIN")

                        .requestMatchers("/api/sections/**")
                        .hasAnyRole("TEACHER", "SUPER_ADMIN")

                        // Super Admin only
                        .requestMatchers("/api/users/**")
                        .hasRole("SUPER_ADMIN")

                        // Everything els
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // ============================
    // PASSWORD ENCODER
    // ============================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // ============================
    // AUTHENTICATION MANAGER
    // ============================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
             {

        return configuration.getAuthenticationManager();
    }
}