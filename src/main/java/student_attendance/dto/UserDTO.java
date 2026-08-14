package student_attendance.dto;

import lombok.Getter;
import lombok.Setter;

import student_attendance.model.Role;

@Getter
@Setter
public class UserDTO {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

    private boolean active;
}