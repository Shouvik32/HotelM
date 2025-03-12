package com.backendproject.hotel_system.Dtos;


import com.backendproject.hotel_system.Models.Booking;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class BookingResponse {

    private Booking booking;
    private ResponseStatus responseStatus;
}
