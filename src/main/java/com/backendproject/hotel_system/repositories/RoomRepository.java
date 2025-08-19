package com.backendproject.hotel_system.repositories;

import com.backendproject.hotel_system.Models.Room;
import com.backendproject.hotel_system.Models.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
   public List<Room> findByRoomType(RoomType roomType);
   public Room save(Room room);
   public List<Room> findRoomsByCapacity(int capacity);
   public List<Room> findRoomsByHotelId(Long hotelId);
   public List<Room> findByHotelIdAndRoomType(Long hotelId, RoomType roomType);
   //public List<Room> findRoomsByHotelIdandCapacity(long id,int capacity);
}
