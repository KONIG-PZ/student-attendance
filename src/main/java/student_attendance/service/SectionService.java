package student_attendance.service;

import org.springframework.stereotype.Service;

import student_attendance.model.Role;
import student_attendance.model.Section;
import student_attendance.model.UserAccount;
import student_attendance.repository.SectionRepository;
import student_attendance.repository.UserAccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final UserAccountRepository userAccountRepository;

    public SectionService(SectionRepository sectionRepository, UserAccountRepository userAccountRepository) {
        this.sectionRepository = sectionRepository;
        this.userAccountRepository = userAccountRepository;
    }

    // Get all Sections
    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    // Get only active Sections
    public List<Section> getActiveSections() {
        return sectionRepository.findByActiveTrue();
    }

    // Get Section by ID
    public Optional<Section> getSectionById(int id) {
        return sectionRepository.findById(id);
    }

    // Add Section
    public Section addSection(Section section) {
        if (sectionRepository.existsBySectionName(section.getSectionName())) {
            throw new RuntimeException("Section name already exists");
        }

        return sectionRepository.save(section);
    }

    // Update Section
    public Section updateSection(int id, Section sectionDetails) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        section.setSectionName(sectionDetails.getSectionName());
        section.setCourse(sectionDetails.getCourse());
        section.setYearLevel(sectionDetails.getYearLevel());
        section.setAcademicYear(sectionDetails.getAcademicYear());
        section.setActive(sectionDetails.isActive());

        return sectionRepository.save(section);
    }

    // Delete Section
    public void deleteSection(int id) {
        if (!sectionRepository.existsById(id)) {
            throw new RuntimeException("Section not found");
        }

        sectionRepository.deleteById(id);
    }

    // Assign a Teacher to a Section
    public Section assignTeacherToSection(int sectionId, Long teacherId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        UserAccount teacher = userAccountRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher account not found"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new RuntimeException("User is not a TEACHER");
        }

        section.getTeachers().add(teacher);
        return sectionRepository.save(section);
    }

    // Remove a Teacher from a Section
    public Section removeTeacherFromSection(int sectionId, Long teacherId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        UserAccount teacher = userAccountRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher account not found"));

        section.getTeachers().remove(teacher);
        return sectionRepository.save(section);
    }

    // Get all Sections assigned to a given teacher (by username)
    public List<Section> getSectionsByTeacherUsername(String username) {
        UserAccount teacher = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Teacher account not found"));

        return new ArrayList<>(teacher.getSections());
    }
}