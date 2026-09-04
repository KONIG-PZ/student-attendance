package student_attendance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sections")
@Getter
@Setter

public class Section {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;

    private String sectionName;

    private String course;

    private String yearLevel;

    private String academicYear;

    private boolean active;

    @ManyToMany
    @JoinTable(
            name = "teacher_section",
            joinColumns = @JoinColumn(name = "section_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    @JsonIgnore
    private Set<UserAccount> teachers = new HashSet<>();
}