package student_attendance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import student_attendance.dto.AdminOverviewDTO;
import student_attendance.model.Role;
import student_attendance.repository.SectionRepository;
import student_attendance.repository.StudentRepository;
import student_attendance.repository.UserAccountRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final StudentRepository studentRepository;
    private final UserAccountRepository userAccountRepository;
    private final SectionRepository sectionRepository;

    public AdminController(
            StudentRepository studentRepository,
            UserAccountRepository userAccountRepository,
            SectionRepository sectionRepository) {

        this.studentRepository = studentRepository;
        this.userAccountRepository = userAccountRepository;
        this.sectionRepository = sectionRepository;
    }

    //GET - System-wide totals for the Super Admin dashboard
    @GetMapping("/overview")
    public ResponseEntity<AdminOverviewDTO> getOverview() {
        long activeStudents = studentRepository.countByActiveTrue();
        long inactiveStudents = studentRepository.countByActiveFalse();

        long totalTeachers = userAccountRepository.countByRole(Role.TEACHER);

        long activeSections = sectionRepository.countByActiveTrue();
        long inactiveSections = sectionRepository.countByActiveFalse();

        AdminOverviewDTO overview = new AdminOverviewDTO(
                activeStudents + inactiveStudents,
                activeStudents,
                inactiveStudents,
                totalTeachers,
                activeSections + inactiveSections,
                activeSections,
                inactiveSections
        );

        return ResponseEntity.ok(overview);
    }
}
