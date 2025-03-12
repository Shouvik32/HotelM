package com.backendproject.hotel_system.Exceptions;

public class CustomerSessionNotFoundException extends RuntimeException {
    public CustomerSessionNotFoundException(String message) {
        super(message);
    }
}
