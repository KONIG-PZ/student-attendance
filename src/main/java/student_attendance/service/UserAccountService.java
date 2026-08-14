package student_attendance.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import student_attendance.dto.UserDTO;
import student_attendance.model.UserAccount;
import student_attendance.repository.UserAccountRepository;

import java.util.List;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder) {

        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ============================
    // CREATE USER
    // ============================

    public UserDTO createUser(UserAccount user) {

        if (userAccountRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userAccountRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        user.setActive(true);

        UserAccount savedUser =
                userAccountRepository.save(user);

        return convertToDTO(savedUser);
    }

    // ============================
    // GET ALL USERS
    // ============================

    public List<UserDTO> getAllUsers() {

        return userAccountRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ============================
    // GET USER BY ID
    // ============================

    public UserDTO getUserById(Long id) {

        UserAccount user = userAccountRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return convertToDTO(user);
    }

    // ============================
    // UPDATE USER
    // ============================

    public UserDTO updateUser(
            Long id,
            UserAccount userDetails) {

        UserAccount user = userAccountRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());
        user.setActive(userDetails.isActive());

        // Only update password if provided
        if (userDetails.getPassword() != null
                && !userDetails.getPassword().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(
                            userDetails.getPassword()
                    )
            );
        }

        UserAccount updatedUser =
                userAccountRepository.save(user);

        return convertToDTO(updatedUser);
    }

    // ============================
    // DEACTIVATE USER
    // ============================

    public void deactivateUser(Long id) {

        UserAccount user = userAccountRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setActive(false);

        userAccountRepository.save(user);
    }

    // ============================
    // DELETE USER
    // ============================

    public void deleteUser(Long id) {

        if (!userAccountRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }

        userAccountRepository.deleteById(id);
    }

    // ============================
    // ENTITY → DTO
    // ============================

    private UserDTO convertToDTO(UserAccount user) {

        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());

        return dto;
    }
}