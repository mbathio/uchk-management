package com.uchk.university.controller;

import com.uchk.university.dto.StudentDto;
import com.uchk.university.entity.Student;
import com.uchk.university.security.SecurityUtils;
import com.uchk.university.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final SecurityUtils securityUtils;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'FORMATION_MANAGER')")
    public ResponseEntity<StudentDto> createStudent(@jakarta.validation.Valid @RequestBody StudentDto studentDto) {
        Student student = studentService.createStudent(studentDto);
        return new ResponseEntity<>(convertToDto(student), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'FORMATION_MANAGER', 'TEACHER') or " +
                  "@studentService.isCurrentUserStudent(#id)")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(convertToDto(student));
    }

    @GetMapping("/studentId/{studentId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'FORMATION_MANAGER', 'TEACHER')")
    public ResponseEntity<StudentDto> getStudentByStudentId(@PathVariable String studentId) {
        Student student = studentService.getStudentByStudentId(studentId);
        return ResponseEntity.ok(convertToDto(student));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'FORMATION_MANAGER', 'TEACHER')")
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        List<StudentDto> studentDtos = students.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentDtos);
    }

    @GetMapping("/formation/{formationId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'FORMATION_MANAGER', 'TEACHER')")
    public ResponseEntity<List<StudentDto>> getStudentsByFormation(@PathVariable Long formationId) {
        List<Student> students = studentService.getStudentsByFormation(formationId);
        List<StudentDto> studentDtos = students.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentDtos);
    }

    @GetMapping("/promo/{promo}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'FORMATION_MANAGER', 'TEACHER')")
    public ResponseEntity<List<StudentDto>> getStudentsByPromo(@PathVariable String promo) {
        List<Student> students = studentService.getStudentsByPromo(promo);
        List<StudentDto> studentDtos = students.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentDtos);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<StudentDto> getCurrentStudentProfile() {
        String username = securityUtils.getCurrentUsername();
        Student student = studentService.getStudentByUsername(username);
        return ResponseEntity.ok(convertToDto(student));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'FORMATION_MANAGER') or " +
                 "@studentService.isCurrentUserStudent(#id)")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @jakarta.validation.Valid @RequestBody StudentDto studentDto) {
        Student student = studentService.updateStudent(id, studentDto);
        return ResponseEntity.ok(convertToDto(student));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    private StudentDto convertToDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setFirstName(student.getUser().getUsername());
        dto.setEmail(student.getUser().getEmail());
        dto.setStudentId(student.getStudentId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        
        // Convert LocalDate to java.util.Date if not null
        if (student.getBirthDate() != null) {
            dto.setBirthDate(java.util.Date.from(student.getBirthDate().atStartOfDay()
                .atZone(java.time.ZoneId.systemDefault())
                .toInstant()));
        }
        
        if (student.getCurrentFormation() != null) {
            dto.setFormationId(student.getCurrentFormation().getId());
            dto.setFormationName(student.getCurrentFormation().getName());
        }
        
        dto.setPromo(student.getPromo());
        dto.setStartYear(student.getStartYear());
        dto.setEndYear(student.getEndYear());
        
        return dto;
    }
}