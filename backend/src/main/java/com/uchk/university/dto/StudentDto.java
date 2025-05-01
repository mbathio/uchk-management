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
    
    private Long formationId;  // Changed from currentFormationId for consistency with method names
    private String formationName;
    
    @NotBlank(message = "Promo is required")
    @Size(max = 20, message = "Promo must not exceed 20 characters")
    private String promo;
    
    @NotNull(message = "Start year is required")
    private Integer startYear;
    
    private Integer endYear;
    
    private List<Long> formationHistoryIds;
    
    private String email;
    private String password;

    public void setFormationId(Long id) {
        this.formationId = id;
    }

    public Long getFormationId() {
        return this.formationId;
    }

    public void setFormationName(String name) {
        this.formationName = name;
    }

    public void setBirthDate(Date date) {
        if (date != null) {
            this.birthDate = new java.sql.Date(date.getTime()).toLocalDate();
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }
}