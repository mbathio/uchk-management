package com.uchk.university.repository;

import com.uchk.university.entity.Document;
import com.uchk.university.entity.DocumentType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    List<Document> findByType(DocumentType type);
    
    List<Document> findByVisibilityLevel(String level);
    
    List<Document> findByCreatorId(Long creatorId);
    
    List<Document> findByCreatedAtAfter(LocalDateTime date);
    
    @Query("SELECT d FROM Document d JOIN d.formations f WHERE f.id = :formationId")
    List<Document> findByFormationId(@Param("formationId") Long formationId);
    
    @Query("SELECT d FROM Document d WHERE LOWER(d.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(d.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Document> searchDocuments(@Param("search") String search);
    
    @Query("SELECT d FROM Document d WHERE d.visibilityLevel <= :visibilityLevel")
    List<Document> findByMaxVisibilityLevel(@Param("visibilityLevel") Integer visibilityLevel);
    
    boolean existsByTitle(String title);
}