package student_attendance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import student_attendance.dto.UserDTO;
import student_attendance.model.UserAccount;
import student_attendance.service.UserAccountService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(
            UserAccountService userAccountService) {

        this.userAccountService = userAccountService;
    }

    // CREATE USER

    @PostMapping
    public ResponseEntity<?> createUser(
            @RequestBody UserAccount user) {

        try {

            UserDTO createdUser =
                    userAccountService.createUser(user);

            return ResponseEntity.ok(createdUser);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // GET ALL USERS

    @GetMapping
    public List<UserDTO> getAllUsers() {

        return userAccountService.getAllUsers();
    }

    // GET USER BY ID

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable Long id) {

        try {

            return ResponseEntity.ok(
                    userAccountService.getUserById(id)
            );

        } catch (RuntimeException e) {

            return ResponseEntity.notFound().build();
        }
    }

    // UPDATE USER

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserAccount user) {

        try {

            return ResponseEntity.ok(
                    userAccountService.updateUser(id, user)
            );

        } catch (RuntimeException e) {

            return ResponseEntity.notFound().build();
        }
    }

    // ============================
    // DEACTIVATE USER
    // ============================

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(
            @PathVariable Long id) {

        try {

            userAccountService.deactivateUser(id);

            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {

            return ResponseEntity.notFound().build();
        }
    }

    // DELETE USER

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        try {

            userAccountService.deleteUser(id);

            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {

            return ResponseEntity.notFound().build();
        }
    }
}