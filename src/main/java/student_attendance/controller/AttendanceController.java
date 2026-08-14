package student_attendance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import student_attendance.dto.AttendanceDTO;
import student_attendance.service.AttendanceService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // TIME IN / TIME OUT

    @PostMapping("/scan")
    public ResponseEntity<AttendanceDTO> scanStudent(
            @RequestParam String studentNumber) {

        try {

            AttendanceDTO attendance =
                    attendanceService.scanStudent(studentNumber);

            return ResponseEntity.ok(attendance);

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().build();
        }
    }

    // GET ALL ATTENDANCE

    @GetMapping
    public List<AttendanceDTO> getAllAttendance() {

        return attendanceService.getAllAttendance();
    }

    // GET TODAY'S ATTENDANCE

    @GetMapping("/today")
    public List<AttendanceDTO> getTodayAttendance() {

        return attendanceService.getTodayAttendance();
    }

    // GET ATTENDANCE BY DATE

    @GetMapping("/date/{date}")
    public List<AttendanceDTO> getAttendanceByDate(
            @PathVariable String date) {

        LocalDate localDate = LocalDate.parse(date);

        return attendanceService.getAttendanceByDate(localDate);
    }

    // GET STUDENT ATTENDANCE

    @GetMapping("/student/{studentId}")
    public List<AttendanceDTO> getStudentAttendance(
            @PathVariable Long studentId) {

        return attendanceService.getStudentAttendance(studentId);
    }
}