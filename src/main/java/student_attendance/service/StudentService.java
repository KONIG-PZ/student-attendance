package student_attendance.service;

import org.springframework.stereotype.Service;

import student_attendance.model.Section;
import student_attendance.model.Student;
import student_attendance.repository.SectionRepository;
import student_attendance.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final SectionRepository sectionRepository;

    public StudentService(StudentRepository studentRepository, SectionRepository sectionRepository) {
        this.studentRepository = studentRepository;
        this.sectionRepository = sectionRepository;
    }

    // Get all Students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    //Get Student by ID
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    //Add Student
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    // Update Student
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setStudentNumber(studentDetails.getStudentNumber());
        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setMiddleName(studentDetails.getMiddleName());
        student.setEmail(studentDetails.getEmail());
        student.setCourse(studentDetails.getCourse());
        student.setYearLevel(studentDetails.getYearLevel());
        student.setSection(studentDetails.getSection());
        student.setQrCode(studentDetails.getQrCode());
        student.setActive(studentDetails.isActive());

        return studentRepository.save(student);
    }

    // Delete Student
    public void deleteStudent(Long id) {
        if(!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found");
        }

        studentRepository.deleteById(id);
    }

    // Assign a Student to a Section
    public Student assignSectionToStudent(Long studentId, int sectionId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        student.setSection(section);
        return studentRepository.save(student);
    }

    // Remove a Student's Section (set back to null)
    public Student removeSectionFromStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setSection(null);
        return studentRepository.save(student);
    }
}