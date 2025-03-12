package com.backendproject.hotel_system.bookingStrategies;

import com.backendproject.hotel_system.Models.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MaxGuestCapacitySuggestion implements  RoomSuggestionStrategy {

    @Override
    public List<Room> suggestRooms(int noOfGuests, List<Room> availableRooms) {
        List<Room> sortedRooms = new ArrayList<>(availableRooms);

        Collections.sort(sortedRooms, new Comparator<Room>() {
            @Override
            public int compare(Room room1, Room room2) {
                return Integer.compare(room2.getCapacity(), room1.getCapacity());
            }
        });

        List<Room> suggestedRooms = new ArrayList<>();
        int remainingGuests = noOfGuests;

        for (Room room : sortedRooms) {
            if (remainingGuests <= 0) break;

            suggestedRooms.add(room);
            remainingGuests -= 1;
        }

        return suggestedRooms;
    }
}
