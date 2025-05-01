package com.uchk.university.config;

import com.uchk.university.security.JwtAuthenticationFilter;
import com.uchk.university.service.CustomUserDetailsService;
import com.uchk.university.security.UnauthorizedEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;
    private final UnauthorizedEntryPoint unauthorizedEntryPoint;

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().configurationSource(corsConfigurationSource()).and()
            .authorizeHttpRequests(authorize -> authorize
                // Public endpoints
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                
                // Document endpoints
                .requestMatchers(HttpMethod.GET, "/documents/types").authenticated()
                .requestMatchers(HttpMethod.GET, "/documents/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/documents/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/documents/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/documents/**").hasAnyAuthority("ADMIN", "TEACHER", "FORMATION_MANAGER")
                
                // Formation endpoints
                .requestMatchers(HttpMethod.GET, "/formations/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/formations/**").hasAnyAuthority("ADMIN", "FORMATION_MANAGER")
                .requestMatchers(HttpMethod.PUT, "/formations/**").hasAnyAuthority("ADMIN", "FORMATION_MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/formations/**").hasAnyAuthority("ADMIN", "FORMATION_MANAGER")
                
                // Student endpoints
                .requestMatchers(HttpMethod.GET, "/students/me").hasAuthority("STUDENT")
                .requestMatchers(HttpMethod.GET, "/students/**").hasAnyAuthority("ADMIN", "ADMINISTRATION", "FORMATION_MANAGER", "TEACHER")
                .requestMatchers(HttpMethod.POST, "/students/**").hasAnyAuthority("ADMIN", "ADMINISTRATION", "FORMATION_MANAGER")
                .requestMatchers(HttpMethod.PUT, "/students/**").hasAnyAuthority("ADMIN", "ADMINISTRATION", "FORMATION_MANAGER", "STUDENT")
                .requestMatchers(HttpMethod.DELETE, "/students/**").hasAnyAuthority("ADMIN", "ADMINISTRATION")
                
                // Staff endpoints
                .requestMatchers(HttpMethod.GET, "/staff/**").hasAnyAuthority("ADMIN", "ADMINISTRATION", "FORMATION_MANAGER", "TEACHER")
                .requestMatchers(HttpMethod.POST, "/staff/**").hasAnyAuthority("ADMIN", "ADMINISTRATION")
                .requestMatchers(HttpMethod.PUT, "/staff/**").hasAnyAuthority("ADMIN", "ADMINISTRATION")
                .requestMatchers(HttpMethod.DELETE, "/staff/**").hasAuthority("ADMIN")
                
                // User endpoints
                .requestMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority("ADMIN", "ADMINISTRATION")
                .requestMatchers(HttpMethod.POST, "/users/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/users/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("ADMIN")
                
                // Notification endpoints
                .requestMatchers("/notifications/**").authenticated()
                
                // Default rule
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(unauthorizedEntryPoint)
            );
            
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}