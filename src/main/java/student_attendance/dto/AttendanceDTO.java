package student_attendance.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AttendanceDTO {

    private Long id;

    private Long studentId;
    private String studentNumber;

    private String studentName;

    private LocalDate date;

    private LocalTime timeIn;
    private LocalTime timeOut;

    private String status;
    private String remarks;
}