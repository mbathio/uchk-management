package com.uchk.university.repository;

import com.uchk.university.entity.Formation;
import com.uchk.university.entity.Student;
import com.uchk.university.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByStudentId(String studentId);
    
    Optional<Student> findByUserId(Long userId);
    
    List<Student> findByPromo(String promo);
    
    List<Student> findByStartYear(Integer startYear);
    
    List<Student> findByCurrentFormationId(Long currentFormationId);
    
    @Query("SELECT s FROM Student s JOIN s.formationHistory f WHERE f.id = :formationId")
    List<Student> findByFormationId(@Param("formationId") Long formationId);
    
    @Query("SELECT s FROM Student s WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(CONCAT(s.firstName, ' ', s.lastName)) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Student> searchByName(@Param("search") String search);
    
    @Query("SELECT DISTINCT s FROM Student s JOIN s.user u WHERE u.username = :username")
    Optional<Student> findByUsername(@Param("username") String username);
    
    boolean existsByStudentId(String studentId);
}