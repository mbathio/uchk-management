package com.uchk.university.service;

import com.uchk.university.dto.StudentDto;
import com.uchk.university.dto.UserDto;
import com.uchk.university.entity.Formation;
import com.uchk.university.entity.Role;
import com.uchk.university.entity.Student;
import com.uchk.university.entity.User;
import com.uchk.university.exception.DuplicateResourceException;
import com.uchk.university.exception.ResourceNotFoundException;
import com.uchk.university.repository.FormationRepository;
import com.uchk.university.repository.StudentRepository;
import com.uchk.university.repository.UserRepository;
import com.uchk.university.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final FormationRepository formationRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SecurityUtils securityUtils;

    public boolean isCurrentUserStudent(Long studentId) {
        try {
            String username = securityUtils.getCurrentUsername();
            Student student = getStudentById(studentId);
            return student.getUser().getUsername().equals(username);
        } catch (Exception e) {
            log.error("Error checking current user as student", e);
            return false;
        }
    }

    @Transactional
    public Student createStudent(StudentDto studentDto) {
        if (userRepository.existsByUsername(studentDto.getFirstName())) {
            throw new DuplicateResourceException("Username already exists");
        }

        User user = userService.createUser(new UserDto(
                null,
                studentDto.getFirstName(),
                studentDto.getPassword(),
                studentDto.getEmail(),
                Role.STUDENT,
                true
        ));

        log.info("User created with ID: {}", user.getId());

        Student student = new Student();
        student.setUser(user);
        student.setStudentId(studentDto.getStudentId());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());

        if (studentDto.getBirthDate() != null) {
            student.setBirthDate(studentDto.getBirthDate());
        } else {
            student.setBirthDate(null);
        }

        // Check if formationId is not null before attempting to get formation
        if (studentDto.getFormationId() != null) {
            Formation formation = formationRepository.findById(((Number)studentDto.getFormationId()).longValue())
                    .orElseThrow(() -> new ResourceNotFoundException("Formation not found with id: " + studentDto.getFormationId()));
            student.setCurrentFormation(formation);
        }

        student.setPromo(studentDto.getPromo());
        student.setStartYear(studentDto.getStartYear());
        student.setEndYear(studentDto.getEndYear());

        Student savedStudent = studentRepository.save(student);
        log.info("Student saved with ID: {}", savedStudent.getId());
        return savedStudent;
    }

    public Student getStudentById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    public Student getStudentByStudentId(String studentId) {
        if (studentId == null || studentId.isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }
        return studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with studentId: " + studentId));
    }

    public Student getStudentByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        // Find student by user ID
        return studentRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for user: " + username));
    }
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Student> getStudentsByFormation(Long formationId) {
        if (formationId == null) {
            throw new IllegalArgumentException("Formation ID cannot be null");
        }
        return studentRepository.findByCurrentFormationId(formationId);
    }

    public List<Student> getStudentsByPromo(String promo) {
        if (promo == null || promo.isEmpty()) {
            throw new IllegalArgumentException("Promo cannot be null or empty");
        }
        return studentRepository.findByPromo(promo);
    }

    @Transactional
    public Student updateStudent(Long id, StudentDto studentDto) {
        Student student = getStudentById(id);

        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());

        if (studentDto.getBirthDate() != null) {
            student.setBirthDate(studentDto.getBirthDate());
        } else {
            student.setBirthDate(null);
        }

        student.setPromo(studentDto.getPromo());
        student.setStartYear(studentDto.getStartYear());
        student.setEndYear(studentDto.getEndYear());

        if (studentDto.getFormationId() != null) {
            Formation formation = formationRepository.findById(((Number)studentDto.getFormationId()).longValue())
                    .orElseThrow(() -> new ResourceNotFoundException("Formation not found with id: " + studentDto.getFormationId()));
            student.setCurrentFormation(formation);
        }

        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
        userService.deleteUser(student.getUser().getId());
    }
}