package com.backendproject.hotel_system.Exceptions;

public class PaswordMismatchError extends RuntimeException {
    public PaswordMismatchError(String message) {
        super(message);
    }
}
