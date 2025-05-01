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
     * Get the current authenticated username
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
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
    
    /**
     * Check if the current user is the owner of the requested user ID
     */
    public boolean isCurrentUser(Long userId) {
        User currentUser = getCurrentUser();
        return currentUser != null && currentUser.getId().equals(userId);
    }
    
    /**
     * Check if the current user has the given username
     */
    public boolean isCurrentUsername(String username) {
        String currentUsername = getCurrentUsername();
        return currentUsername != null && currentUsername.equals(username);
    }
}