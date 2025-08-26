package com.backendproject.hotel_system.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class AccessHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(AccessHandler.class);

    public AccessHandler() {
        log.info("AccessHandler initialized");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("Access denied for request {}: {}", request.getRequestURI(), accessDeniedException.getMessage());
        if (response.isCommitted()) {
            log.warn("Response already committed, cannot write AccessDenied response for {}", request.getRequestURI());
            return;
        }
        sendJson(response, HttpServletResponse.SC_FORBIDDEN, "You are not allowed to access this resource");
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        log.warn("Authentication required for request {}: {}", request.getRequestURI(), authException.getMessage());
        if (response.isCommitted()) {
            log.warn("Response already committed, cannot write AuthenticationEntryPoint response for {}", request.getRequestURI());
            return;
        }
        sendJson(response, HttpServletResponse.SC_UNAUTHORIZED, "Please login");
    }

    private void sendJson(HttpServletResponse response, int statusCode, String message) throws IOException {
        // Set status, content type and encoding BEFORE writing body
        response.resetBuffer(); // optional: clears any data but will throw if committed
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String json = """
           {
             "statusCode": %d,
             "status": "Failed",
             "message": "%s"
           }
           """.formatted(statusCode, escapeJson(message));

        response.getWriter().write(json);
        response.getWriter().flush();
    }
    private String escapeJson(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }
}