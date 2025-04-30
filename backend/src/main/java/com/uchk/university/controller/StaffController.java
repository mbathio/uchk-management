package com.uchk.university.controller;

import com.uchk.university.dto.StaffDto;
import com.uchk.university.entity.Staff;
import com.uchk.university.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
@Tag(name = "Staff Management", description = "API endpoints for staff management")
public class StaffController {
    
    private final StaffService staffService;
    
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION')")
    @Operation(summary = "Create a new staff member", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Staff> createStaff(@RequestBody StaffDto staffDto) {
        Staff staff = staffService.createStaff(staffDto);
        return new ResponseEntity<>(staff, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'FORMATION_MANAGER') or @staffService.isOwnStaffProfile(#id, authentication.name)")
    @Operation(summary = "Get staff by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Staff> getStaffById(@PathVariable Long id, Authentication authentication) {
        Staff staff = staffService.getStaffById(id);
        return ResponseEntity.ok(staff);
    }
    
    @GetMapping("/staffId/{staffId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'FORMATION_MANAGER')")
    @Operation(summary = "Get staff by staffId", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Staff> getStaffByStaffId(@PathVariable String staffId) {
        Staff staff = staffService.getStaffByStaffId(staffId);
        return ResponseEntity.ok(staff);
    }
    
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'FORMATION_MANAGER')")
    @Operation(summary = "Get all staff members", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Staff>> getAllStaff() {
        List<Staff> staff = staffService.getAllStaff();
        return ResponseEntity.ok(staff);
    }
    
    @GetMapping("/department/{department}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'FORMATION_MANAGER')")
    @Operation(summary = "Get staff by department", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Staff>> getStaffByDepartment(@PathVariable String department) {
        List<Staff> staff = staffService.getStaffByDepartment(department);
        return ResponseEntity.ok(staff);
    }
    
    @GetMapping("/position/{position}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'FORMATION_MANAGER')")
    @Operation(summary = "Get staff by position", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Staff>> getStaffByPosition(@PathVariable String position) {
        List<Staff> staff = staffService.getStaffByPosition(position);
        return ResponseEntity.ok(staff);
    }
    
    @GetMapping("/formation/{formationId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'FORMATION_MANAGER', 'STUDENT')")
    @Operation(summary = "Get trainers by formation", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Staff>> getTrainersByFormation(@PathVariable Long formationId) {
        List<Staff> trainers = staffService.getTrainersByFormationId(formationId);
        return ResponseEntity.ok(trainers);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION') or @staffService.isOwnStaffProfile(#id, authentication.name)")
    @Operation(summary = "Update a staff member", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Staff> updateStaff(@PathVariable Long id, @RequestBody StaffDto staffDto, Authentication authentication) {
        Staff staff = staffService.updateStaff(id, staffDto);
        return ResponseEntity.ok(staff);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete a staff member", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}