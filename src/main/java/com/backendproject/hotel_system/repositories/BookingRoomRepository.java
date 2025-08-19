package com.backendproject.hotel_system.repositories;

import com.backendproject.hotel_system.Models.BookingRoom;
import com.backendproject.hotel_system.Models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRoomRepository extends JpaRepository<BookingRoom,Long> {
    boolean existsByRoomAndBooking_CheckInDateLessThanEqualAndBooking_CheckOutDateGreaterThanEqual(
            Room room, Date checkOut, Date checkIn
    );
    @Query("SELECT br FROM BookingRoom br " +
            "WHERE br.room.id = :roomId " +
            "AND (:requestedCheckIn < br.checkOutDate AND :requestedCheckOut > br.checkInDate)")
    List<BookingRoom> findOverlappingBookings(
            @Param("roomId") Long roomId,
            @Param("requestedCheckIn") Date requestedCheckIn,
            @Param("requestedCheckOut") Date requestedCheckOut
    );@Query("SELECT CASE WHEN COUNT(br) > 0 THEN true ELSE false END " +
            "FROM BookingRoom br " +
            "WHERE br.room = :room " +
            "AND (:requestedCheckIn < br.checkOutDate AND :requestedCheckOut > br.checkInDate)")
    boolean existsOverlappingBookingForRoom(
                    @Param("room") Room room,
                    @Param("requestedCheckIn") Date requestedCheckIn,
                    @Param("requestedCheckOut") Date requestedCheckOut
            );

   // List<BookingRoom> saveAll(List<BookingRoom> rooms);
}
