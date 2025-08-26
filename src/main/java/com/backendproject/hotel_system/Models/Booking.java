package com.backendproject.hotel_system.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Booking extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "customersession_id")
    private CustomerSession customerSession;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingRoom> bookingRooms;

    @Temporal(TemporalType.DATE)
    private Date checkInDate;

    @Temporal(TemporalType.DATE)
    private Date checkOutDate;

    @ManyToOne
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
}
