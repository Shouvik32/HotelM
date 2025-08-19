package com.backendproject.hotel_system.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"hotel"})
public class Room extends BaseModel {
     private String roomNumber;
     private double price;

     @Enumerated(EnumType.STRING)
     @Column(name = "room_type")
     private RoomType roomType;
     @ManyToOne
     @JoinColumn(name = "hotel_id")
     private Hotel hotel;
     private String description;
     private int capacity;


}