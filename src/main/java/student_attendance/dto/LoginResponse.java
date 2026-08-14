package student_attendance.dto;

import lombok.Getter;
import lombok.Setter;

import student_attendance.model.Role;

@Getter
@Setter
public class LoginResponse {

    private String token;
    private String username;
    private Role role;

    public LoginResponse(
            String token,
            String username,
            Role role) {

        this.token = token;
        this.username = username;
        this.role = role;
    }
}