package student_attendance.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import student_attendance.model.Role;
import student_attendance.model.UserAccount;
import student_attendance.repository.UserAccountRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createInitialAdmin(
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String username = "it.admin";

            // Don't create duplicate admin
            if (userAccountRepository
                    .findByUsername(username)
                    .isPresent()) {

                return;
            }

            UserAccount admin = new UserAccount();

            admin.setUsername(username);
            admin.setPassword(
                    passwordEncoder.encode("admin123")
            );

            admin.setFirstName("IT");
            admin.setLastName("Administrator");
            admin.setEmail("admin@school.edu");

            admin.setRole(Role.SUPER_ADMIN);
            admin.setActive(true);

            userAccountRepository.save(admin);

            System.out.println(
                    "===================================="
            );
            System.out.println(
                    "INITIAL SUPER ADMIN CREATED"
            );
            System.out.println(
                    "Username: it.admin"
            );
            System.out.println(
                    "Password: admin123"
            );
            System.out.println(
                    "===================================="
            );
        };
    }
}