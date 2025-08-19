package com.backendproject.hotel_system.Strategies.RoomSuggestionStrategies;

import com.backendproject.hotel_system.Dtos.Requests.RoomRequestDto;
import com.backendproject.hotel_system.Dtos.Requests.SearchRoomRequestDto;
import com.backendproject.hotel_system.Dtos.Requests.SuggestRoomRequest;
import com.backendproject.hotel_system.Exceptions.RoomNotFoundException;
import com.backendproject.hotel_system.Models.Room;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Component
public class MaxGuestCapacitySuggestion implements RoomSuggestionStrategy {

    @Override
    public List<Room> suggestRooms(SuggestRoomRequest searchRoomRequestDto, List<Room> availableRooms) {
        if (availableRooms == null || availableRooms.isEmpty()) {
            System.out.println(availableRooms+"test");
            throw new RoomNotFoundException("No available rooms found for suggestion");
        }
        List<Room> sortedRooms = new ArrayList<>(availableRooms);
        int requiredGuests = searchRoomRequestDto.getNoOfGuests();
        sortedRooms.sort(Comparator.comparingInt(Room::getCapacity).reversed());

        List<Room> suggestedRooms = new ArrayList<>();
        int remainingGuests = requiredGuests;

        for (Room room : sortedRooms) {
            if (room.getCapacity() < 1) continue;
            if (room.getCapacity() <= remainingGuests) {
                suggestedRooms.add(room);
                remainingGuests -= room.getCapacity();
                break;
            }
        }
        if (remainingGuests > 0) {
            sortedRooms.sort(Comparator.comparingInt(Room::getCapacity));
            for (Room room : sortedRooms) {
                while (remainingGuests > 0 && room.getCapacity() <= remainingGuests) {
                    suggestedRooms.add(room);
                    remainingGuests -= room.getCapacity();
                }

            }
            if (remainingGuests > 0 && !sortedRooms.isEmpty()) {
                suggestedRooms.add(sortedRooms.get(0));
            }
        }

        return suggestedRooms;
    }
}