package com.uchk.university.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormationDto {
    
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;
    
    @NotBlank(message = "Type is required")
    @Size(max = 50, message = "Type must not exceed 50 characters")
    private String type;
    
    @NotBlank(message = "Level is required")
    @Size(max = 50, message = "Level must not exceed 50 characters")
    private String level;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    private BigDecimal fundingAmount;
    
    @Size(max = 100, message = "Funding type must not exceed 100 characters")
    private String fundingType;
    
    private List<Long> staffIds;
    
    private List<Long> studentIds;
    
    private List<Long> documentIds;
    
    
}