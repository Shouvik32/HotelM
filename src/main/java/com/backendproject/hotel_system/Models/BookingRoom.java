package com.backendproject.hotel_system.Models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class BookingRoom extends BaseModel   {

    @ManyToOne
    private Booking booking;
    @ManyToOne
    private Room room;
    private Integer bookedRoomsCount;
}
