package student_attendance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import student_attendance.model.Section;
import student_attendance.service.SectionService;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor

public class SectionController {

    private final SectionService sectionService;

    //Get all Sections
    @GetMapping
    public ResponseEntity<List<Section>> getAllSections() {
        return ResponseEntity.ok(sectionService.getAllSections());
    }

    //Get Section by ID
    @GetMapping("/{id}")
    public ResponseEntity<Section> getSectionById(@PathVariable int id) {

            return sectionService.getSectionById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    // CREATE section
    @PostMapping
    public ResponseEntity<?> addSection(@RequestBody Section sectionDetails) {
        try {
            return ResponseEntity.ok(sectionService.addSection(sectionDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // UPDATE section
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSection(
            @PathVariable int id,
            @RequestBody Section sectionDetails) {

        try {
            return ResponseEntity.ok(sectionService.updateSection(id, sectionDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete section
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSection(@PathVariable int id) {
        try {
            sectionService.deleteSection(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // POST - Assign a teacher to a section
    @PostMapping("/{sectionId}/teachers/{teacherId}")
    public ResponseEntity<?> assignTeacher(@PathVariable int sectionId, @PathVariable Long teacherId) {
        try {
            Section updated = sectionService.assignTeacherToSection(sectionId, teacherId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE - Remove a teacher from a section
    @DeleteMapping("/{sectionId}/teachers/{teacherId}")
    public ResponseEntity<?> removeTeacher(@PathVariable int sectionId, @PathVariable Long teacherId) {
        try {
            Section updated = sectionService.removeTeacherFromSection(sectionId, teacherId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
