package com.backendproject.hotel_system.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.List;
import java.util.Map;
@Getter @Setter
@Data
@NoArgsConstructor
@Entity
public class Invoice extends BaseModel{
    private long bookingId;
    @OneToMany
    private List<BookingRoom> bookedRooms;
    private double totalAmount;
    private double gst;
    private double serviceCharge;
    @ManyToOne
    private CustomerSession customerSession;
}
