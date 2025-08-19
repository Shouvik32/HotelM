package com.backendproject.hotel_system.Exceptions;

public class RoomAlreadyBookedForDatesException extends RuntimeException {
    public RoomAlreadyBookedForDatesException(String message) {
        super(message);
    }
}
