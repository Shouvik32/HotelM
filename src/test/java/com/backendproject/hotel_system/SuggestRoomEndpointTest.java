package com.backendproject.hotel_system;

import com.backendproject.hotel_system.Dtos.Requests.SuggestRoomRequest;
import com.backendproject.hotel_system.Dtos.Responses.RoomResponseDto;
import com.backendproject.hotel_system.Models.Room;
import com.backendproject.hotel_system.Models.RoomType;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class SuggestRoomEndpointTest {

    @Test
    public void testRoomResponseDtoFromMethod() {
        // Test that RoomResponseDto.from() handles null properly
        RoomResponseDto dto = RoomResponseDto.from(null);
        assertNull(dto);
        
        // Test that RoomResponseDto.from() works with valid Room
        Room room = new Room();
        room.setId(1L);
        room.setRoomNumber("101");
        room.setRoomType(RoomType.SINGLE_BED);
        room.setPrice(100.0);
        room.setCapacity(2);
        room.setDescription("Test room");
        
        RoomResponseDto responseDto = RoomResponseDto.from(room);
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals("101", responseDto.getRoomNumber());
        assertEquals(RoomType.SINGLE_BED, responseDto.getRoomType());
        assertEquals(100.0, responseDto.getPrice());
        assertEquals(2, responseDto.getCapacity());
        assertEquals("Test room", responseDto.getDescription());
    }
    
    @Test
    public void testSuggestRoomRequestCreation() {
        // Test that SuggestRoomRequest can be created properly
        Date checkin = new Date();
        Date checkout = new Date(System.currentTimeMillis() + 86400000); // +1 day
        
        SuggestRoomRequest request = new SuggestRoomRequest(2, checkin, checkout);
        assertEquals(2, request.getNoOfGuests());
        assertEquals(checkin, request.getCheckin());
        assertEquals(checkout, request.getCheckout());
    }
}