package com.uchk.university.service;

import com.uchk.university.dto.FormationDto;
import com.uchk.university.entity.Formation;
import com.uchk.university.entity.Staff;
import com.uchk.university.exception.ResourceNotFoundException;
import com.uchk.university.repository.FormationRepository;
import com.uchk.university.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormationService {
    private final FormationRepository formationRepository;
    private final StaffRepository staffRepository;

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATION_MANAGER')")
    public Formation createFormation(Formation formation) {
        // Validate the formation data
        validateFormation(formation);
        return formationRepository.save(formation);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATION_MANAGER')")
    public Formation createFormation(FormationDto formationDto) {
        // Create a new Formation entity from the DTO
        Formation formation = new Formation();
        formation.setName(formationDto.getName());
        formation.setType(formationDto.getType());
        formation.setLevel(formationDto.getLevel());
        formation.setStartDate(formationDto.getStartDate());
        formation.setEndDate(formationDto.getEndDate());
        formation.setDescription(formationDto.getDescription());
        formation.setFundingAmount(formationDto.getFundingAmount());
        formation.setFundingType(formationDto.getFundingType());
        
        // Validate and save the formation
        validateFormation(formation);
        Formation savedFormation = formationRepository.save(formation);
        
        // Process related entities if present in the DTO
        if (formationDto.getStaffIds() != null && !formationDto.getStaffIds().isEmpty()) {
            formationDto.getStaffIds().forEach(staffId -> {
                try {
                    assignStaffToFormation(savedFormation.getId(), staffId);
                } catch (Exception e) {
                    log.warn("Failed to assign staff {} to formation {}: {}", 
                             staffId, savedFormation.getId(), e.getMessage());
                }
            });
        }
        
        return savedFormation;
    }

    @Transactional(readOnly = true)
    public Formation getFormationById(Long id) {
        return formationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Formation not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Formation> getAllFormations() {
        return formationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Formation> getFormationsByType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Formation type cannot be empty");
        }
        return formationRepository.findByType(type);
    }

    @Transactional(readOnly = true)
    public List<Formation> getFormationsByLevel(String level) {
        if (level == null || level.trim().isEmpty()) {
            throw new IllegalArgumentException("Formation level cannot be empty");
        }
        return formationRepository.findByLevel(level);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATION_MANAGER')")
    public Formation updateFormation(Long id, FormationDto formationDto) {
        Formation formation = getFormationById(id);
        
        // Validate the updated formation data
        validateFormation(formationDto);
        
        // Update the formation properties
        formation.setName(formationDto.getName());
        formation.setType(formationDto.getType());
        formation.setLevel(formationDto.getLevel());
        formation.setStartDate(formationDto.getStartDate());
        formation.setEndDate(formationDto.getEndDate());
        formation.setDescription(formationDto.getDescription());
        formation.setFundingAmount(formationDto.getFundingAmount());
        formation.setFundingType(formationDto.getFundingType());
        
        return formationRepository.save(formation);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteFormation(Long id) {
        Formation formation = getFormationById(id);
        // Consider checking for related entities that might be affected by deletion
        formationRepository.delete(formation);
    }

    @Transactional
@PreAuthorize("hasAnyRole('ADMIN', 'FORMATION_MANAGER')")
public void assignStaffToFormation(Long formationId, Long staffId) {
    Formation formation = formationRepository.findById(formationId)
            .orElseThrow(() -> new ResourceNotFoundException("Formation not found with id: " + formationId));
    
    Staff staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));
    
    // Check if the staff is already assigned to this formation
    if (formation.getStaff() != null && formation.getStaff().contains(staff)) {
        log.info("Staff {} is already assigned to formation {}", staffId, formationId);
        return;
    }
    
    // Add staff to formation
    if (formation.getStaff() == null) {
        formation.setStaff(new ArrayList<>());
    }
    formation.getStaff().add(staff);
    
    // Add formation to staff
    if (staff.getFormations() == null) {
        // Create a HashSet instead of ArrayList to match the expected type
        staff.setFormations(new HashSet<>());
    }
    staff.getFormations().add(formation);
    
    // Save changes
    formationRepository.save(formation);
    staffRepository.save(staff);
    
    log.info("Staff {} assigned to formation {}", staffId, formationId);
}
    
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATION_MANAGER')")
    public void removeStaffFromFormation(Long formationId, Long staffId) {
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new ResourceNotFoundException("Formation not found with id: " + formationId));
        
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));
        
        // Remove staff from formation
        if (formation.getStaff() != null) {
            boolean removed = formation.getStaff().removeIf(s -> s.getId().equals(staffId));
            if (!removed) {
                log.info("Staff {} was not assigned to formation {}", staffId, formationId);
                return;
            }
        }
        
        // Remove formation from staff
        if (staff.getFormations() != null) {
            staff.getFormations().removeIf(f -> f.getId().equals(formationId));
        }
        
        // Save changes
        formationRepository.save(formation);
        staffRepository.save(staff);
        
        log.info("Staff {} removed from formation {}", staffId, formationId);
    }
    
    /**
     * Get the schedule for a specific formation
     */
    @Transactional(readOnly = true)
    public List<Object> getFormationSchedule(Long formationId) {
        // Verify formation exists
        getFormationById(formationId);
        
        // Implement schedule retrieval logic
        // For now return empty list
        return new ArrayList<>();
    }
    
    /**
     * Get trainers/staff for a specific formation
     */
    @Transactional(readOnly = true)
    public List<Staff> getTrainersByFormationId(Long formationId) {
        Formation formation = formationRepository.findById(formationId)
            .orElseThrow(() -> new ResourceNotFoundException("Formation not found with id: " + formationId));

  // Fix: Convert ArrayList to HashSet for setFormations method
        List<Staff> trainers = staffRepository.findByFormations(formation);
        for (Staff staff : trainers) {
            if (staff.getFormations() == null) {
                // Create a new HashSet instead of ArrayList to match the expected type
                Set<Formation> formationSet = new HashSet<>(); 
                formationSet.add(formation);
                staff.setFormations(formationSet);
            }
        }
        return trainers;    }
    
    private void validateFormation(Formation formation) {
        if (formation.getName() == null || formation.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Formation name cannot be empty");
        }
        
        if (formation.getType() == null || formation.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Formation type cannot be empty");
        }
        
        if (formation.getLevel() == null || formation.getLevel().trim().isEmpty()) {
            throw new IllegalArgumentException("Formation level cannot be empty");
        }
        
        if (formation.getStartDate() == null) {
            throw new IllegalArgumentException("Formation start date cannot be null");
        }
        
        // Check that end date is after start date if provided
        if (formation.getEndDate() != null && formation.getStartDate().isAfter(formation.getEndDate())) {
            throw new IllegalArgumentException("Formation end date must be after start date");
        }
    }

    private void validateFormation(FormationDto formationDto) {
        // Validate FormationDto specific checks
        if (formationDto.getName() == null || formationDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Formation name cannot be empty");
        }
        
        if (formationDto.getType() == null || formationDto.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Formation type cannot be empty");
        }
        
        if (formationDto.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required");
        }
        
        if (formationDto.getEndDate() != null && formationDto.getEndDate().isBefore(formationDto.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }
}