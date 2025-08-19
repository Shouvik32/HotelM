package com.backendproject.hotel_system.Exceptions;

public class RoomAlreadyBookedException extends IllegalArgumentException {
    public RoomAlreadyBookedException(String message) {
        super(message);
    }
}
