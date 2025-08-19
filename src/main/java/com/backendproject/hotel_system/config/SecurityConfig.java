package com.backendproject.hotel_system.config;

import com.backendproject.hotel_system.Filters.JwtAuthenticationFilter;
import com.backendproject.hotel_system.repositories.TokenRepository;
import com.backendproject.hotel_system.services.AuthService;
import com.backendproject.hotel_system.services.RolewisePermissions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final AuthService authService;
    private final TokenRepository tokenRepository;
    private final RolewisePermissions rolewisePermissions;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            AuthService authService,
            TokenRepository tokenRepository,
            RolewisePermissions rolewisePermissions,
            JwtAuthenticationFilter jwtAuthenticationFilter // Inject filter here
    ) {
        this.authService = authService;
        this.tokenRepository = tokenRepository;
        this.rolewisePermissions = rolewisePermissions;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/auth/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Use injected bean

        return http.build();
    }
}
