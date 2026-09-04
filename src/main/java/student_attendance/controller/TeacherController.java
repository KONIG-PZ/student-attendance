package student_attendance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import student_attendance.model.Section;
import student_attendance.service.SectionService;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final SectionService sectionService;

    public TeacherController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    // GET - Sections assigned to the currently logged-in teacher
    @GetMapping("/my-sections")
    public ResponseEntity<List<Section>> getMySections(Authentication authentication) {
        String username = authentication.getName();
        List<Section> sections = sectionService.getSectionsByTeacherUsername(username);
        return ResponseEntity.ok(sections);
    }
}