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

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import student_attendance.security.JwtAuthenticationFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // SECURITY FILTER CHAIN

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(auth -> auth

                        // Public login
                        .requestMatchers("/api/auth/**")
                        .permitAll()

                        // Teacher + Super Admin
                        .requestMatchers("/api/students/**")
                        .hasAnyRole("TEACHER", "SUPER_ADMIN")

                        .requestMatchers("/api/attendance/**")
                        .hasAnyRole("TEACHER", "SUPER_ADMIN")

                        // Sections: Teachers can view, only Super Admin can create/edit/delete
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/sections/**")
                        .hasAnyRole("TEACHER", "SUPER_ADMIN")

                        .requestMatchers("/api/sections/**")
                        .hasRole("SUPER_ADMIN")

                        // Reports: viewable by Teacher and Super Admin
                        .requestMatchers("/api/reports/**")
                        .hasAnyRole("TEACHER", "SUPER_ADMIN")

                        // Teacher-only self-service endpoints
                        .requestMatchers("/api/teacher/**")
                        .hasRole("TEACHER")

                        // Super Admin only
                        .requestMatchers("/api/users/**")
                        .hasRole("SUPER_ADMIN")

                        .requestMatchers("/api/admin/**")
                        .hasRole("SUPER_ADMIN")

                        // Everything else
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
            throws Exception {

        return configuration.getAuthenticationManager();
    }
}