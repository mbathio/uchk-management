package com.uchk.university.service;

import com.uchk.university.dto.NotificationDto;
import com.uchk.university.entity.Notification;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> getRecentNotificationsForCurrentUser(int limit);    int getUnreadCount();
    NotificationDto getNotification(Long id);
    NotificationDto markAsRead(Long id);
    void markAllAsRead();
    void deleteNotification(Long id);
    List<Notification> getNotificationsByUsername(String name);
    List<Notification> getUnreadNotificationsByUsername(String name);
    Notification getNotificationById(Long id);
    Notification createNotification(NotificationDto notificationDto);
    Notification markNotificationAsRead(Long id);
    void markAllNotificationsAsRead();
}                                                                                                                 