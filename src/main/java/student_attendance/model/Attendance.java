package student_attendance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
@Getter
@Setter
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalTime timeIn;

    private LocalTime timeOut;

    private String status;

    private String remarks;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}