package student_attendance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "students")
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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