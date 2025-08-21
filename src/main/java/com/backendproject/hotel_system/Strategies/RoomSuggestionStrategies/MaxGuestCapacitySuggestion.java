package com.backendproject.hotel_system.Strategies.RoomSuggestionStrategies;

import com.backendproject.hotel_system.Dtos.Requests.SuggestRoomRequest;
import com.backendproject.hotel_system.Exceptions.RoomNotFoundException;
import com.backendproject.hotel_system.Models.Room;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MaxGuestCapacitySuggestion implements RoomSuggestionStrategy {

    @Override
    public List<Room> suggestRooms(SuggestRoomRequest searchRoomRequestDto, List<Room> availableRooms) {
        if (availableRooms == null || availableRooms.isEmpty()) {
            System.out.println("No available rooms found for suggestion");
            throw new RoomNotFoundException("No available rooms found for suggestion");
        }

        int requiredGuests = searchRoomRequestDto.getNoOfGuests();
        Map<Integer, List<Room>> roomsByCapacity = availableRooms.stream()
                .filter(room -> room.getCapacity() > 0)
                .collect(Collectors.groupingBy(Room::getCapacity));

        List<Room> suggestedRooms = new ArrayList<>();
        int remainingGuests = requiredGuests;

        List<Integer> sortedCapacities = roomsByCapacity.keySet().stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        System.out.println("Sorted capacities (desc): " + sortedCapacities);

        for (Integer capacity : sortedCapacities) {
            List<Room> roomsWithThisCapacity = roomsByCapacity.get(capacity);

            int roomsNeeded = remainingGuests / capacity;
            int availableRoomsOfThisCapacity = roomsWithThisCapacity.size();
            int roomsToTake = Math.min(roomsNeeded, availableRoomsOfThisCapacity);
            for (int i = 0; i < roomsToTake; i++) {
                suggestedRooms.add(roomsWithThisCapacity.get(i));
                remainingGuests -= capacity;
                System.out.println("Added room: " + roomsWithThisCapacity.get(i).getRoomNumber() +
                        " (capacity: " + capacity + "), Remaining guests: " + remainingGuests);
            }

            if (remainingGuests <= 0) {
                break;
            }
        }

        if (remainingGuests > 0) {

            final int finalRemainingGuests = remainingGuests; // Make it final for lambda

            Room suitableRoom = availableRooms.stream()
                    .filter(room -> room.getCapacity() >= finalRemainingGuests)
                    .filter(room -> !suggestedRooms.contains(room))
                    .min(Comparator.comparingInt(Room::getCapacity))
                    .orElse(null);

            if (suitableRoom != null) {
                suggestedRooms.add(suitableRoom);
                remainingGuests -= suitableRoom.getCapacity();
                System.out.println("Added final room: " + suitableRoom.getRoomNumber() +
                        " (capacity: " + suitableRoom.getCapacity() + "), Remaining guests: " + remainingGuests);
            } else {
                System.out.println("Warning: Could not accommodate all guests. Still need space for " + finalRemainingGuests + " guests.");
            }
        }
        suggestedRooms.forEach(room ->
                System.out.println("- Room " + room.getRoomNumber() + " (capacity: " + room.getCapacity() + ")")
        );

        return suggestedRooms;
    }
}