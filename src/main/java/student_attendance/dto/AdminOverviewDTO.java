package student_attendance.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminOverviewDTO {
    
    private long totalStudents;
    private long activeStudents;
    private long inactiveStudents;

    private long totalTeachers;

    private long totalSections;
    private long activeSections;
    private long inactiveSections;

    public AdminOverviewDTO(
            long totalStudents,
            long activeStudents,
            long inactiveStudents,
            long totalTeachers,
            long totalSections,
            long activeSections,
            long inactiveSections) {

        this.totalStudents = totalStudents;
        this.activeStudents = activeStudents;
        this.inactiveStudents = inactiveStudents;
        this.totalTeachers = totalTeachers;
        this.totalSections = totalSections;
        this.activeSections = activeSections;
        this.inactiveSections = inactiveSections;
    }
}
