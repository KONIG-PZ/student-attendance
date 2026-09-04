package student_attendance.service;

import org.springframework.stereotype.Service;

import student_attendance.dto.SectionReportDayDTO;
import student_attendance.dto.StudentDayStatusDTO;
import student_attendance.model.Attendance;
import student_attendance.model.Student;
import student_attendance.repository.AttendanceRepository;
import student_attendance.repository.StudentRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    public ReportService(StudentRepository studentRepository, AttendanceRepository attendanceRepository) {
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    // Attendance report for one Section across a date range (inclusive).
    // For each date, every enrolled student gets a status:
    // PRESENT / LATE (from their Attendance record) or ABSENT (no record found).
    public List<SectionReportDayDTO> getSectionReport(int sectionId, LocalDate startDate, LocalDate endDate) {

        if (endDate.isBefore(startDate)) {
            throw new RuntimeException("endDate cannot be before startDate");
        }

        List<Student> students = studentRepository.findBySectionId(sectionId);

        if (students.isEmpty()) {
            throw new RuntimeException("No students found for this section");
        }

        List<SectionReportDayDTO> report = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

            List<StudentDayStatusDTO> dayStatuses = new ArrayList<>();

            for (Student student : students) {

                Optional<Attendance> attendance =
                        attendanceRepository.findByStudentIdAndDate(student.getId(), date);

                String status = attendance.map(Attendance::getStatus).orElse("ABSENT");

                dayStatuses.add(new StudentDayStatusDTO(
                        student.getId(),
                        student.getStudentNumber(),
                        student.getFirstName() + " " + student.getLastName(),
                        status,
                        attendance.map(Attendance::getTimeIn).orElse(null),
                        attendance.map(Attendance::getTimeOut).orElse(null)
                ));
            }

            report.add(new SectionReportDayDTO(date, dayStatuses));
        }

        return report;
    }
}