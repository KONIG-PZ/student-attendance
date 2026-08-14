package student_attendance.service;

import org.springframework.stereotype.Service;

import student_attendance.dto.AttendanceDTO;
import student_attendance.model.Attendance;
import student_attendance.model.Student;
import student_attendance.repository.AttendanceRepository;
import student_attendance.repository.StudentRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            StudentRepository studentRepository) {

        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    // TIME IN / TIME OUT

    public AttendanceDTO scanStudent(String studentNumber) {

        // Find student
        Student student = studentRepository
                .findByStudentNumber(studentNumber)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        // Check if student is active
        if (!student.isActive()) {
            throw new RuntimeException("Student is inactive");
        }

        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // Check if student already has attendance today
        var existingAttendance =
                attendanceRepository.findByStudentIdAndDate(
                        student.getId(),
                        today
                );

        // TIME IN

        if (existingAttendance.isEmpty()) {

            Attendance attendance = new Attendance();

            attendance.setStudent(student);
            attendance.setDate(today);
            attendance.setTimeIn(currentTime);
            attendance.setStatus("PRESENT");

            Attendance savedAttendance =
                    attendanceRepository.save(attendance);

            return convertToDTO(savedAttendance);
        }

        Attendance attendance = existingAttendance.get();

        // TIME OUT

        if (attendance.getTimeOut() == null) {

            attendance.setTimeOut(currentTime);

            Attendance savedAttendance =
                    attendanceRepository.save(attendance);

            return convertToDTO(savedAttendance);
        }

        // ALREADY COMPLETED

        throw new RuntimeException(
                "Student has already timed in and out today"
        );
    }

    // GET ALL ATTENDANCE

    public List<AttendanceDTO> getAllAttendance() {

        return attendanceRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // GET TODAY'S ATTENDANCE

    public List<AttendanceDTO> getTodayAttendance() {

        return attendanceRepository
                .findByDate(LocalDate.now())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // GET ATTENDANCE BY DATE


    public List<AttendanceDTO> getAttendanceByDate(LocalDate date) {

        return attendanceRepository
                .findByDate(date)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // GET STUDENT ATTENDANCE

    public List<AttendanceDTO> getStudentAttendance(Long studentId) {

        return attendanceRepository
                .findByStudentId(studentId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ENTITY → DTO

    private AttendanceDTO convertToDTO(Attendance attendance) {

        AttendanceDTO dto = new AttendanceDTO();

        dto.setId(attendance.getId());

        dto.setStudentId(
                attendance.getStudent().getId()
        );

        dto.setStudentNumber(
                attendance.getStudent().getStudentNumber()
        );

        dto.setStudentName(
                attendance.getStudent().getFirstName()
                        + " "
                        + attendance.getStudent().getLastName()
        );

        dto.setDate(attendance.getDate());

        dto.setTimeIn(
                attendance.getTimeIn()
        );

        dto.setTimeOut(
                attendance.getTimeOut()
        );

        dto.setStatus(
                attendance.getStatus()
        );

        dto.setRemarks(
                attendance.getRemarks()
        );

        return dto;
    }
}