package com.backendproject.hotel_system.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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

    @Temporal(TemporalType.DATE)
    private Date checkInDate;

    @Temporal(TemporalType.DATE)
    private Date checkOutDate;

}
