package com.backendproject.hotel_system.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter @Setter
@Data
@NoArgsConstructor
@Entity
public class Booking extends BaseModel {
     @ManyToOne
     private CustomerSession customerSession;
     @OneToMany
     List<BookingRoom> bookedRooms;
     private LocalDate checkInDate;
     private LocalDate checkOutDate;
}
