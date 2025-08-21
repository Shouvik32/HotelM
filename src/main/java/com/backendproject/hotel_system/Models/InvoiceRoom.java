package com.backendproject.hotel_system.Models;


import lombok.Data;

@Data
public class InvoiceRoom extends BaseModel{
    private Invoice invoice;
    private BookingRoom bookingRoom;
}
