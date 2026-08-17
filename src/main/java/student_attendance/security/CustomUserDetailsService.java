package student_attendance.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import student_attendance.model.UserAccount;
import student_attendance.repository.UserAccountRepository;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    public CustomUserDetailsService(
            UserAccountRepository userAccountRepository) {

        this.userAccountRepository =
                userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(
            String username)
            throws UsernameNotFoundException {

        UserAccount user =
                userAccountRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "User not found"
                                ));

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())

                // VERY IMPORTANT
                .roles(user.getRole().name())

                .disabled(!user.isActive())
                .build();
    }
}