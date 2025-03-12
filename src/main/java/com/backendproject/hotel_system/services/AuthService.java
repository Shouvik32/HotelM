package com.backendproject.hotel_system.services;

import com.backendproject.hotel_system.Models.User;

public interface AuthService {
    User login(String username, String password);
    void logout();
    User register(User user);
    void forgotPassword(String email);
}
