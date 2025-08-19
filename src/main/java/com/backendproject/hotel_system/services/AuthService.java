package com.backendproject.hotel_system.services;

import com.backendproject.hotel_system.Models.Token;
import com.backendproject.hotel_system.Models.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public Token login(String username, String password);
    void forgotPassword(String email);
    User register(User user);
    void logout(String token);
    public  User validateToken(Token token);

}
