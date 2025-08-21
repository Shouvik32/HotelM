package com.backendproject.hotel_system.Filters;

import com.backendproject.hotel_system.Dtos.Responses.ApiResponse;
import com.backendproject.hotel_system.repositories.TokenRepository;
import com.backendproject.hotel_system.services.RolewisePermissions;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${JWT_SECRET}")
    private String JWT_SECRET;
    private final RolewisePermissions rolewisePermissions;
    private final TokenRepository tokenRepository;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(RolewisePermissions rolewisePermissions,
                                   TokenRepository tokenRepository,
                                   ObjectMapper objectMapper) {
        this.rolewisePermissions = rolewisePermissions;
        this.tokenRepository = tokenRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        String tokenString = authHeader.substring(7);
        try {
            var tokenOp = tokenRepository.findByToken(tokenString);
            if (tokenOp.isEmpty())
                throw new RuntimeException("Invalid token.");
            if (tokenOp.get().isHasExpired())
                throw new RuntimeException("Token has expired. Please login again.");
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET.getBytes())
                    .parseClaimsJws(tokenString)
                    .getBody();
            String userId = claims.getSubject();
            List<String> permissions = claims.get("permissions", List.class);
            if (permissions != null && !permissions.isEmpty()) {
                List<GrantedAuthority> authorities = permissions
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            ApiResponse<String> apiResponse = new ApiResponse<>("error", e.getMessage(), null);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
            return;
        }
        chain.doFilter(request, response);
    }
}
