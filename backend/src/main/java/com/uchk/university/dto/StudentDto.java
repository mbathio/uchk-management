package com.uchk.university.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    
    private Long id;
    
    private Long userId;
    
    @NotBlank(message = "Student ID is required")
    @Size(max = 20, message = "Student ID must not exceed 20 characters")
    private String studentId;
    
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s'-]+$", message = "First name must contain only letters, spaces, hyphens and apostrophes")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s'-]+$", message = "Last name must contain only letters, spaces, hyphens and apostrophes")
    private String lastName;
    
    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;
    
    private Long currentFormationId;
    
    @NotBlank(message = "Promo is required")
    @Size(max = 20, message = "Promo must not exceed 20 characters")
    private String promo;
    
    @NotNull(message = "Start year is required")
    private Integer startYear;
    
    private Integer endYear;
    
    private List<Long> formationHistoryIds;

    public void setFormationId(Long id2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFormationId'");
    }

    public void setFormationName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFormationName'");
    }

    public void setBirthDate(Date from) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBirthDate'");
    }

    public void setEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEmail'");
    }

    public String getPassword() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPassword'");
    }

    public String getEmail() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEmail'");
    }

    public Object getFormationId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFormationId'");
    }
}