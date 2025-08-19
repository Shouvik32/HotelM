package com.backendproject.hotel_system.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
     @JsonIgnore // Prevent circular reference during JSON serialization
     private Hotel hotel;
     
     private String description;
     private int capacity;


}