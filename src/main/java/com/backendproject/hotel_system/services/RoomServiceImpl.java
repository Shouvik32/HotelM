package com.backendproject.hotel_system.services;

import com.backendproject.hotel_system.Dtos.Requests.RoomRequestDto;
import com.backendproject.hotel_system.Dtos.Requests.SuggestRoomRequest;
import com.backendproject.hotel_system.Exceptions.RoomNotFoundException;
import com.backendproject.hotel_system.Models.Hotel;
import com.backendproject.hotel_system.Models.Room;


import com.backendproject.hotel_system.Models.RoomType;
import com.backendproject.hotel_system.Strategies.RoomSuggestionStrategies.RoomSuggestionStrategy;
import com.backendproject.hotel_system.repositories.RoomRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
public class RoomServiceImpl implements RoomService{
    @Autowired
    private RoomRepository roomRepository;
    private RoomSuggestionStrategy roomSuggestionStrategy;
    @Override
    public List<Room> getAllRoomsByHotel(long hotelId) {
        return roomRepository.findRoomsByHotelId(hotelId);
    }
    @Override
    public Room getRoomById(long id) {
        Optional<Room> room = roomRepository.findById(id);
        if(room.isEmpty()) throw new RoomNotFoundException("Room not found");
        return room.get();
    }
    @Override
    public Room save(String roomNumber, double price, String description, String type, int capacity, Hotel hotel, boolean isAvailable) {
        Room room=new Room();
        room.setRoomNumber(roomNumber);
        room.setRoomType(RoomType.valueOf(String.valueOf(type)));
        room.setPrice(price);
        room.setDescription(description);
        room.setCapacity(capacity);
        room.setHotel(hotel);
        return roomRepository.save(room);

    }
    @Override
    public Room updateRoom(Room updatedRoom) {

        Optional<Room> roomop = roomRepository.findById(updatedRoom.getId());
        if (roomop.isEmpty()) {
            throw new RoomNotFoundException("Room not found");
        }
        Room room = roomop.get();
        if (updatedRoom.getRoomNumber() != null) {
            room.setRoomNumber(updatedRoom.getRoomNumber());
        }
        if(updatedRoom.getRoomType() != null) {
            room.setRoomType(updatedRoom.getRoomType());
        }
        if (updatedRoom.getPrice()!=0.0){
            room.setPrice(updatedRoom.getPrice());
        }
        if(updatedRoom.getDescription()!=null) {
            room.setDescription(updatedRoom.getDescription());
        }
        if(updatedRoom.getCapacity()!=0) {
            room.setCapacity(updatedRoom.getCapacity());
        }
        room.setUpdatedAt(new Date());
        return roomRepository.save(room);
    }
    @Override
    public Room deleteRoom(long id) {
        Optional<Room> roomop=roomRepository.findById(id);
        if(roomop.isEmpty()) {
            throw new RoomNotFoundException("Room not found");
        }
        Room room=roomop.get();
        roomRepository.delete(room);
        return room;
    }
    public  List<Room> getRoomByType(long hotelId,String  roomType) {
        List<Room> getRoom = roomRepository.findByHotelIdAndRoomType(hotelId,RoomType.valueOf(roomType.toUpperCase()));
        if (getRoom == null || getRoom.isEmpty()) {
            throw new RoomNotFoundException("No rooms found for type: " + roomType);
        }
        return getRoom;
    }
    public List<Room> suggestedRooms(SuggestRoomRequest roomRequestDto, List<Room> availableRooms){
        return roomSuggestionStrategy.suggestRooms(roomRequestDto, availableRooms);
    }
    }


