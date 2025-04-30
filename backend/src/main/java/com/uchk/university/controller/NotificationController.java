package com.uchk.university.controller;

import com.uchk.university.entity.Notification;
import com.uchk.university.entity.User;
import com.uchk.university.exception.ResourceNotFoundException;
import com.uchk.university.service.NotificationService;
import com.uchk.university.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notifications", description = "API pour la gestion des notifications")
public class NotificationController {
    
    private final NotificationService notificationService;
    private final UserService userService;
    
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer toutes les notifications de l'utilisateur connecté")
    public ResponseEntity<List<Notification>> getUserNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.getUserByUsername(userDetails.getUsername());
            List<Notification> notifications = notificationService.getNotificationsForUser(user.getId());
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            log.error("Error fetching notifications: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/unread")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer les notifications non lues de l'utilisateur connecté")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.getUserByUsername(userDetails.getUsername());
            List<Notification> notifications = notificationService.getUnreadNotificationsForUser(user.getId());
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            log.error("Error fetching unread notifications: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Marquer une notification comme lue")
    public ResponseEntity<Notification> markNotificationAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Vérifier que la notification appartient à l'utilisateur connecté
            User user = userService.getUserByUsername(userDetails.getUsername());
            List<Notification> userNotifications = notificationService.getNotificationsForUser(user.getId());
            
            boolean belongsToUser = userNotifications.stream()
                    .anyMatch(n -> n.getId().equals(id));
            
            if (!belongsToUser) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            Notification notification = notificationService.markNotificationAsRead(id);
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            log.error("Error marking notification as read: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/mark-all-as-read")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Marquer toutes les notifications comme lues")
    public ResponseEntity<Void> markAllNotificationsAsRead(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.getUserByUsername(userDetails.getUsername());
            notificationService.markAllNotificationsAsRead(user.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error marking all notifications as read: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Supprimer une notification")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Vérifier que la notification appartient à l'utilisateur connecté
            User user = userService.getUserByUsername(userDetails.getUsername());
            List<Notification> userNotifications = notificationService.getNotificationsForUser(user.getId());
            
            boolean belongsToUser = userNotifications.stream()
                    .anyMatch(n -> n.getId().equals(id));
            
            if (!belongsToUser) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            notificationService.deleteNotification(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting notification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/user")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Supprimer toutes les notifications d'un utilisateur")
    public ResponseEntity<Void> deleteAllNotificationsForUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.getUserByUsername(userDetails.getUsername());
            notificationService.getNotificationsForUser(user.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting all notifications for user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/user")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Supprimer toutes les notifications d'un utilisateur")
    public ResponseEntity<Void> deleteNotificationsForUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.getUserByUsername(userDetails.getUsername());
            notificationService.getNotificationsForUser(user.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting all notifications for user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }   
}
