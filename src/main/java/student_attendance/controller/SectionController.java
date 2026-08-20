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
    public ResponseEntity<Section> addSection(@RequestBody Section sectionDetails){
        return ResponseEntity.ok(sectionService.addSection(sectionDetails));
    }

    // UPDATE section
    @PutMapping("/{id}")
    public ResponseEntity<Section> updateSection(
            @PathVariable int id,
            @RequestBody Section sectionDetails){

        return ResponseEntity.ok(
                sectionService.updateSection(id, sectionDetails)
        );
    }

    // Delete section
    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteSection(@PathVariable int id){

        sectionService.deleteSection(id);

        return ResponseEntity.noContent().build();
    }

}
