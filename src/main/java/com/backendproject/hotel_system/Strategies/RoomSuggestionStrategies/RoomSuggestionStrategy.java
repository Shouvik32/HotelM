package com.backendproject.hotel_system.Strategies.RoomSuggestionStrategies;



import com.backendproject.hotel_system.Dtos.Requests.RoomRequestDto;
import com.backendproject.hotel_system.Dtos.Requests.SearchRoomRequestDto;
import com.backendproject.hotel_system.Dtos.Requests.SuggestRoomRequest;
import com.backendproject.hotel_system.Models.Room;

import java.util.List;

public interface RoomSuggestionStrategy {
    public List<Room> suggestRooms(SuggestRoomRequest searchRoomRequestDto, List<Room> availableRooms);
}
