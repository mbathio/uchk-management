package com.uchk.university.controller;

import com.uchk.university.dto.NotificationDto;
import com.uchk.university.entity.Notification;
import com.uchk.university.service.NotificationService;
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
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Management", description = "API endpoints for notification management")
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'FORMATION_MANAGER')")
    @Operation(summary = "Create a new notification", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Notification> createNotification(@RequestBody NotificationDto notificationDto) {
        Notification notification = notificationService.createNotification(notificationDto);
        return new ResponseEntity<>(notification, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION') or @notificationService.isNotificationRecipient(#id)")
    @Operation(summary = "Get notification by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }
    
    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get current user's notifications", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Notification>> getCurrentUserNotifications(Authentication authentication) {
        List<Notification> notifications = notificationService.getNotificationsByUsername(authentication.getName());
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/unread")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get current user's unread notifications", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Notification>> getCurrentUserUnreadNotifications(Authentication authentication) {
        List<Notification> notifications = notificationService.getUnreadNotificationsByUsername(authentication.getName());
        return ResponseEntity.ok(notifications);
    }
    
    @PutMapping("/{id}/mark-as-read")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION') or @notificationService.isNotificationRecipient(#id)")
    @Operation(summary = "Mark notification as read", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Notification> markNotificationAsRead(@PathVariable Long id) {
        Notification notification = notificationService.markNotificationAsRead(id);
        return ResponseEntity.ok(notification);
    }
    
    @PutMapping("/mark-all-as-read")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Mark all notifications as read", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> markAllNotificationsAsRead() {
        notificationService.markAllNotificationsAsRead();
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION') or @notificationService.isNotificationCreator(#id)")
    @Operation(summary = "Delete notification", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok().build();
    }
}