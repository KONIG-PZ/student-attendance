package student_attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import student_attendance.model.Section;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {

    Optional<Section> findBySectionName(String sectionName);

    List<Section> findByActiveTrue();

    boolean existsBySectionName(String sectionName);

    long countByActiveTrue();

    long countByActiveFalse();
}