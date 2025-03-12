package com.backendproject.hotel_system.bookingStrategies;



import com.backendproject.hotel_system.Models.Room;

import java.util.List;

public interface RoomSuggestionStrategy {
    public List<Room> suggestRooms(int noOfGuests, List<Room> availableRooms);
}
