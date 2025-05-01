package com.uchk.university.repository;

import com.uchk.university.entity.Formation;
import com.uchk.university.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    
    Optional<Staff> findByStaffId(String staffId);
    
    Optional<Staff> findByUserId(Long userId);
    
    List<Staff> findByDepartment(String department);
    
    List<Staff> findByPosition(String position);
    
    @Query("SELECT s FROM Staff s JOIN s.formations f WHERE f.id = :formationId")
    List<Staff> findByFormationId(@Param("formationId") Long formationId);
    
    @Query("SELECT s FROM Staff s WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(CONCAT(s.firstName, ' ', s.lastName)) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Staff> searchByName(@Param("search") String search);
    
    @Query("SELECT DISTINCT s FROM Staff s JOIN s.user u WHERE u.username = :username")
    Optional<Staff> findByUsername(@Param("username") String username);
    
    boolean existsByStaffId(String staffId);

    List<Staff> findByFormationId(Formation formation);

    List<Staff> findByFormations(Formation formation);
}