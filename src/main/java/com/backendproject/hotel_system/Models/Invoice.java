package com.backendproject.hotel_system.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Invoice extends BaseModel {
    private long bookingId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id") // foreign key in BookingRoom table
    private List<BookingRoom> bookingRooms;

    private double totalAmount;
    private double gst;
    private double serviceCharge;

    @ManyToOne
    private CustomerSession customerSession;

    @ManyToOne
    private Hotel hotel;
}
