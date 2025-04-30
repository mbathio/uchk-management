package com.uchk.university.service;

import com.uchk.university.entity.Notification;
import com.uchk.university.entity.Role;
import com.uchk.university.entity.User;
import com.uchk.university.exception.ResourceNotFoundException;
import com.uchk.university.repository.NotificationRepository;
import com.uchk.university.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Notification createNotification(User user, String message, String type) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        
        log.debug("Creating notification for user {}: {}", user.getUsername(), message);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotificationsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        log.debug("Fetching all notifications for user: {}", user.getUsername());
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public List<Notification> getUnreadNotificationsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        log.debug("Fetching unread notifications for user: {}", user.getUsername());
        return notificationRepository.findByUserAndReadFalseOrderByCreatedAtDesc(user);
    }

    @Override
    @Transactional
    public Notification markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        
        notification.setRead(true);
        log.debug("Marking notification as read: {}", notificationId);
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllNotificationsAsRead(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        List<Notification> unreadNotifications = notificationRepository.findByUserAndReadFalseOrderByCreatedAtDesc(user);
        unreadNotifications.forEach(notification -> notification.setRead(true));
        
        notificationRepository.saveAll(unreadNotifications);
        log.debug("Marked all notifications as read for user: {}", user.getUsername());
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new ResourceNotFoundException("Notification not found with id: " + notificationId);
        }
        
        notificationRepository.deleteById(notificationId);
        log.debug("Deleted notification: {}", notificationId);
    }

    @Override
    @Transactional
    public void notifyDocumentCreation(User creator, String documentTitle, Long documentId) {
        String message = "Nouveau document créé: " + documentTitle;
        
        // Envoyer notification au créateur
        createNotification(creator, "Vous avez créé un nouveau document: " + documentTitle, "DOCUMENT_CREATED");
        
        // Envoyer notification aux administrateurs
        List<User> admins = userRepository.findByRole(Role.ADMIN)
                .stream()
                .filter(admin -> !admin.getId().equals(creator.getId()))
                .collect(Collectors.toList());
        
        admins.forEach(admin -> {
            createNotification(admin, message + " (par " + creator.getUsername() + ")", "DOCUMENT_CREATED");
        });
        
        log.info("Document creation notifications sent for document: {} (id: {})", documentTitle, documentId);
    }

    @Override
    @Transactional
    public void notifyDocumentUpdate(User updater, String documentTitle, Long documentId) {
        String message = "Document mis à jour: " + documentTitle;
        
        // Envoyer notification à l'utilisateur qui a mis à jour
        createNotification(updater, "Vous avez mis à jour le document: " + documentTitle, "DOCUMENT_UPDATED");
        
        // Envoyer notification aux administrateurs
        List<User> admins = userRepository.findByRole(Role.ADMIN)
                .stream()
                .filter(admin -> !admin.getId().equals(updater.getId()))
                .collect(Collectors.toList());
        
        admins.forEach(admin -> {
            createNotification(admin, message + " (par " + updater.getUsername() + ")", "DOCUMENT_UPDATED");
        });
        
        log.info("Document update notifications sent for document: {} (id: {})", documentTitle, documentId);
    }
}