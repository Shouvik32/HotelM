package com.backendproject.hotel_system.Exceptions;

public class RoomNotFoundException extends RuntimeException {
  public RoomNotFoundException(String message) {
    super(message);
  }
}
