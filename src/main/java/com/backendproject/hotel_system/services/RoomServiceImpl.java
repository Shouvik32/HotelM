package com.backendproject.hotel_system.services;

import com.backendproject.hotel_system.Exceptions.RoomNotFoundException;
import com.backendproject.hotel_system.Models.Room;


import com.backendproject.hotel_system.Models.RoomType;
import com.backendproject.hotel_system.repositories.RoomRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
public class RoomServiceImpl implements RoomService{
    //private RestTemplate restTemplate;
    private RoomRepository roomRepository;

    public RoomServiceImpl( RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    @Override
    public Room getRoomById(long id) {
        return roomRepository.findById(id).get();
    }
    @Override
    public Room addRoom( String roomNumber, double price, String description, RoomType type,int capacity) {
        Room room=new Room();
        room.setRoomNumber(roomNumber);
        room.setRoomType(RoomType.valueOf(String.valueOf(type)));
        room.setPrice(price);
        room.setDescription(description);
        room.setCapacity(capacity);
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

}
