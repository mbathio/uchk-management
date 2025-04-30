package com.uchk.university.controller;

import com.uchk.university.dto.FormationDto;
import com.uchk.university.entity.Formation;
import com.uchk.university.service.FormationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/formations")
@RequiredArgsConstructor
@Tag(name = "Formation Management", description = "API endpoints for formation management")
public class FormationController {
    
    private final FormationService formationService;
    
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'FORMATION_MANAGER')")
    @Operation(summary = "Create a new formation", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Formation> createFormation(@RequestBody FormationDto formationDto) {
        Formation formation = formationService.createFormation(formationDto);
        return new ResponseEntity<>(formation, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get formation by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Formation> getFormationById(@PathVariable Long id) {
        Formation formation = formationService.getFormationById(id);
        return ResponseEntity.ok(formation);
    }
    
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all formations", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Formation>> getAllFormations() {
        List<Formation> formations = formationService.getAllFormations();
        return ResponseEntity.ok(formations);
    }
    
    @GetMapping("/type/{type}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get formations by type", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Formation>> getFormationsByType(@PathVariable String type) {
        List<Formation> formations = formationService.getFormationsByType(type);
        return ResponseEntity.ok(formations);
    }
    
    @GetMapping("/level/{level}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get formations by level", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Formation>> getFormationsByLevel(@PathVariable String level) {
        List<Formation> formations = formationService.getFormationsByLevel(level);
        return ResponseEntity.ok(formations);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'FORMATION_MANAGER')")
    @Operation(summary = "Update a formation", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Formation> updateFormation(@PathVariable Long id, @RequestBody FormationDto formationDto) {
        Formation formation = formationService.updateFormation(id, formationDto);
        return ResponseEntity.ok(formation);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'FORMATION_MANAGER')")
    @Operation(summary = "Delete a formation", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteFormation(@PathVariable Long id) {
        formationService.deleteFormation(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{formationId}/assign-staff/{staffId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'FORMATION_MANAGER')")
    @Operation(summary = "Assign staff to formation", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> assignStaffToFormation(@PathVariable Long formationId, @PathVariable Long staffId) {
        formationService.assignStaffToFormation(formationId, staffId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{formationId}/remove-staff/{staffId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'FORMATION_MANAGER')")
    @Operation(summary = "Remove staff from formation", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> removeStaffFromFormation(@PathVariable Long formationId, @PathVariable Long staffId) {
        formationService.removeStaffFromFormation(formationId, staffId);
        return ResponseEntity.ok().build();
    }
}