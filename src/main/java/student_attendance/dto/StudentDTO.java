package student_attendance.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDTO {

    private Long id;

    private String studentNumber;
    private String firstName;
    private String lastName;
    private String middleName;

    private String email;
    private String course;
    private String yearLevel;
    private String section;

    private String qrCode;

    private boolean active;
}