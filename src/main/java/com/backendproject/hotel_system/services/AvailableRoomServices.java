package com.backendproject.hotel_system.services;

import com.backendproject.hotel_system.Models.Room;



public interface AvailableRoomServices {

    public Room addRoom(Room room);
    public Room updateRoom(Room room);
    public void deleteRoom(Room room);
    public Room getRoom(Room room);
}
