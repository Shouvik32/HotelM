package com.backendproject.hotel_system.Dtos.Requests;

import com.backendproject.hotel_system.Models.BaseModel;
import com.backendproject.hotel_system.Models.RoomType;
import lombok.*;

import java.util.Date;

@Data

public class RoomRequestDto extends BaseModel {
    private RoomType roomType;
    private String roomNumber;
    private String description;
    private Double price;
    int capacity;
    int noOfGuests;
    Date checkin;
    Date checkout;


}
