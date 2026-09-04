package student_attendance.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SectionReportDayDTO {

    private LocalDate date;
    private List<StudentDayStatusDTO> students;

    public SectionReportDayDTO(LocalDate date, List<StudentDayStatusDTO> students) {
        this.date = date;
        this.students = students;
    }
}
