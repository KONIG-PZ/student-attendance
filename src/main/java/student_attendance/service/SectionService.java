package student_attendance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import student_attendance.model.Section;
import student_attendance.repository.SectionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;

    // Get all Sections
    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    // Get Section by ID
    public Optional<Section> getSectionById(int id) {
        return sectionRepository.findById((long) id);
    }

    //Add Section
    public Section addSection(Section section){
        return sectionRepository.save(section);
    };

    //Update Section
    public Section updateSection(int id, Section sectionDetails){

        Section section = sectionRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        section.setSectionName(sectionDetails.getSectionName());
        section.setCourse(sectionDetails.getCourse());
        section.setYearLevel(sectionDetails.getYearLevel());
        section.setAcademicYear(sectionDetails.getAcademicYear());
        section.setActive(sectionDetails.isActive());

        return sectionRepository.save(section);
    }

    // Delete Section
    public void deleteSection(int id){

    if(!sectionRepository.existsById((long) id)){
        throw new RuntimeException("Section not found");
    }

    sectionRepository.deleteById((long) id);
    }

}
