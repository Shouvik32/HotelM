package com.backendproject.hotel_system.Dtos.Requests;

import com.backendproject.hotel_system.Models.RoomType;
import lombok.Data;

import java.util.Date;


@Data
public class SearchRoomRequestDto {
    int noOfGuests;
    private Date checkin;
    private Date checkout;

}
