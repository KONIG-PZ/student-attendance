package student_attendance.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class StudentDayStatusDTO {

    private Long studentId;
    private String studentNumber;
    private String studentName;

    // PRESENT, LATE, or ABSENT
    private String status;

    private LocalTime timeIn;
    private LocalTime timeOut;

    public StudentDayStatusDTO(
            Long studentId,
            String studentNumber,
            String studentName,
            String status,
            LocalTime timeIn,
            LocalTime timeOut) {

        this.studentId = studentId;
        this.studentNumber = studentNumber;
        this.studentName = studentName;
        this.status = status;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }
}