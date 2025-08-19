package com.backendproject.hotel_system.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Booking extends BaseModel {
     @ManyToOne
     @JoinColumn(name = "customersession_id")
     private CustomerSession customerSession;
     @OneToMany
     List<BookingRoom> bookedRooms;
     private Date checkInDate;//yrs
     private Date checkOutDate;
}
