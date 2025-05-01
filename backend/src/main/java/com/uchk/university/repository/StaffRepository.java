package com.uchk.university.repository;

import com.uchk.university.entity.Formation;
import com.uchk.university.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    
    Optional<Staff> findByStaffId(String staffId);
    
    Optional<Staff> findByUserId(Long userId);
    
    List<Staff> findByDepartment(String department);
    
    List<Staff> findByPosition(String position);
    
    List<Staff> findByFormations(Formation formation);
    
    List<Staff> findByFormations_Name(String formationName);
}