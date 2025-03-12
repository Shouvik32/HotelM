package com.backendproject.hotel_system.Dtos;

import com.backendproject.hotel_system.Models.BaseModel;
import com.backendproject.hotel_system.Models.RoomType;
import lombok.*;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchRoomRequestDto extends BaseModel {
    private RoomType roomType;
    private String roomNumber;



    private String description;
    private Double price;
    int capacity;
    int noOfGuests;
    Date checkin;
    Date checkout;

    public RoomType getRoomType() {
        return roomType;
    }

    public String getDescription() {
        return description;
    }
    public String getRoomNumber() {
        return roomNumber;
    }

    public Double getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getNoOfGuests() {
        return noOfGuests;
    }

    public Date getCheckin() {
        return checkin;
    }

    public Date getCheckout() {
        return checkout;
    }
}
