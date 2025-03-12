package com.backendproject.hotel_system.Exceptions;

public class UnAuthorizesAccess extends RuntimeException {
    public UnAuthorizesAccess(String message) {
        super(message);
    }
}
