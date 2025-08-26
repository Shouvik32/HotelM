package com.backendproject.hotel_system.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRoom extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "booking_id") // foreign key
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    private Integer bookedRoomsCount;
    private double price;
    @Temporal(TemporalType.DATE)
    private Date checkInDate;

    @Temporal(TemporalType.DATE)
    private Date checkOutDate;
}

