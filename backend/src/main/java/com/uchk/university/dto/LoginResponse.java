package com.uchk.university.dto;

import com.uchk.university.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    private String token;
    private String username;
    private String email;
    private Role role;
    private Long userId;
    private Long expiresIn;
}