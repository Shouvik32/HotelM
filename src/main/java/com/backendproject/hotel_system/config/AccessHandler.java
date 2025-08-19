package com.backendproject.hotel_system.config;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.AuthenticationException;
import java.io.IOException;

@Component
public class AccessHandler implements AccessDeniedHandler,AuthenticationEntryPoint {

    public AccessHandler() {
        System.out.println("Handler called");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        sendJson(response, HttpServletResponse.SC_FORBIDDEN,"You are not allowed to access this resource");
    }
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        sendJson(response, HttpServletResponse.SC_UNAUTHORIZED,"Please login");
    }
    private void sendJson(HttpServletResponse response, int statusCode,String message) throws IOException {
        String json = """
           {
              "statusCode": %d,
              "status": "Failed",
              "message": "%s"
            }
       \s""".formatted(statusCode, message);
        response.setContentType("application/json");

        response.getWriter().write(json);
    }


}