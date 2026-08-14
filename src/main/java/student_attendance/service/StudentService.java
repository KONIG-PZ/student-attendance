package student_attendance.service;

import org.springframework.stereotype.Service;

import student_attendance.model.Student;
import student_attendance.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
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
}
