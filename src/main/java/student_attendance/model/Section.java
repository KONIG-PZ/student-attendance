package student_attendance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
}
