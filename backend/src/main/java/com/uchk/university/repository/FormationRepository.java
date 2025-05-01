package com.uchk.university.repository;

import com.uchk.university.entity.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    
    List<Formation> findByType(String type);
    
    List<Formation> findByLevel(String level);
    
    List<Formation> findByStartDateAfter(LocalDate date);
    
    List<Formation> findByEndDateBefore(LocalDate date);
    
    @Query("SELECT f FROM Formation f WHERE f.startDate <= :date AND (f.endDate IS NULL OR f.endDate >= :date)")
    List<Formation> findCurrentFormations(@Param("date") LocalDate date);
    
    @Query("SELECT f FROM Formation f JOIN f.staff s WHERE s.id = :staffId")
    List<Formation> findByStaffId(@Param("staffId") Long staffId);
    
    @Query("SELECT f FROM Formation f JOIN f.students s WHERE s.id = :studentId")
    List<Formation> findByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT f FROM Formation f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Formation> searchByName(@Param("search") String search);
    
    boolean existsByName(String name);

    Optional<Formation> findById(Long formationId);

}