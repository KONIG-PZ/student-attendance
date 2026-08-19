package student_attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import student_attendance.model.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
}