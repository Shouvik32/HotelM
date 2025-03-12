package com.backendproject.hotel_system.repositories;

import com.backendproject.hotel_system.Models.Room;
import com.backendproject.hotel_system.Models.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
   public List<Room> findByRoomType(RoomType roomType);

}
