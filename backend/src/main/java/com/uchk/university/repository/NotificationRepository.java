package com.uchk.university.repository;

import com.uchk.university.entity.Notification;
import com.uchk.university.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    /**
     * Trouve toutes les notifications d'un utilisateur, triées par date de création descendante
     */
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    
    /**
     * Trouve toutes les notifications non lues d'un utilisateur
     */
    List<Notification> findByUserAndReadFalse(User user);
    
    /**
     * Trouve toutes les notifications non lues d'un utilisateur, triées par date de création descendante
     */
    List<Notification> findByUserAndReadFalseOrderByCreatedAtDesc(User user);
    
    /**
     * Compte le nombre de notifications non lues d'un utilisateur
     */
    Long countByUserAndReadFalse(User user);
    
    /**
     * Trouve toutes les notifications d'un type spécifique pour un utilisateur
     */
    List<Notification> findByUserAndType(User user, String type);
}