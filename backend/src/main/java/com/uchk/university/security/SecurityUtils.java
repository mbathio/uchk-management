package com.uchk.university.security;

import com.uchk.university.entity.User;
import com.uchk.university.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
    
    private final UserService userService;
    
    /**
     * Check if the current authenticated user is the specified user ID
     */
    public boolean isCurrentUser(Long userId) {
        String currentUsername = getCurrentUsername();
        if (currentUsername == null) {
            return false;
        }
        
        try {
            User user = userService.getUserById(userId);
            return user != null && user.getUsername().equals(currentUsername);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if the current authenticated user has the specified username
     */
    public boolean isCurrentUsername(String username) {
        String currentUsername = getCurrentUsername();
        return currentUsername != null && currentUsername.equals(username);
    }
    
    /**
     * Get the current authenticated username
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        
        return principal.toString();
    }
    
    /**
     * Get the current authenticated user
     */
    public User getCurrentUser() {
        String username = getCurrentUsername();
        if (username == null) {
            return null;
        }
        
        return userService.getUserByUsername(username);
    }
}