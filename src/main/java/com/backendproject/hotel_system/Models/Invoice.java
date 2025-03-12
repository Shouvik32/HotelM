package com.backendproject.hotel_system.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;
import java.util.Map;
@Getter @Setter
@Data
@NoArgsConstructor
@Entity
public class Invoice extends BaseModel{
    @OneToMany
    private List<BookingRoom> bookedRooms;
    private double totalAmount;
    private double gst;
    private double serviceCharge;
}
