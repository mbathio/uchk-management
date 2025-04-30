package com.uchk.university.service;

import com.uchk.university.entity.Notification;
import com.uchk.university.entity.User;

import java.util.List;

public interface NotificationService {
    /**
     * Crée une nouvelle notification pour un utilisateur
     * 
     * @param user Utilisateur destinataire
     * @param message Message de la notification
     * @param type Type de notification
     * @return La notification créée
     */
    Notification createNotification(User user, String message, String type);
    
    /**
     * Récupère toutes les notifications d'un utilisateur
     * 
     * @param userId ID de l'utilisateur
     * @return Liste des notifications
     */
    List<Notification> getNotificationsForUser(Long userId);
    
    /**
     * Récupère toutes les notifications non lues d'un utilisateur
     * 
     * @param userId ID de l'utilisateur
     * @return Liste des notifications non lues
     */
    List<Notification> getUnreadNotificationsForUser(Long userId);
    
    /**
     * Marque une notification comme lue
     * 
     * @param notificationId ID de la notification
     * @return La notification mise à jour
     */
    Notification markNotificationAsRead(Long notificationId);
    
    /**
     * Marque toutes les notifications d'un utilisateur comme lues
     * 
     * @param userId ID de l'utilisateur
     */
    void markAllNotificationsAsRead(Long userId);
    
    /**
     * Supprime une notification
     * 
     * @param notificationId ID de la notification
     */
    void deleteNotification(Long notificationId);
    
    /**
     * Crée une notification pour informer de la création d'un nouveau document
     * 
     * @param creator Créateur du document
     * @param documentTitle Titre du document
     * @param documentId ID du document
     */
    void notifyDocumentCreation(User creator, String documentTitle, Long documentId);
    
    /**
     * Crée une notification pour informer de la mise à jour d'un document
     * 
     * @param updater Utilisateur qui a mis à jour le document
     * @param documentTitle Titre du document
     * @param documentId ID du document
     */
    void notifyDocumentUpdate(User updater, String documentTitle, Long documentId);
}