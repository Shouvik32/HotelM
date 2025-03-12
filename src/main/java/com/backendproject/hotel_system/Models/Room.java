package com.backendproject.hotel_system.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room extends BaseModel {
     private String roomNumber;
     private double price;

     @Enumerated(EnumType.STRING)
     private RoomType roomType;

     private String description;
     private int capacity;

     public String getRoomNumber() {
          return roomNumber;
     }

     public void setRoomNumber(String roomNumber) {
          this.roomNumber = roomNumber;
     }

     public double getPrice() {
          return price;
     }

     public void setPrice(double price) {
          this.price = price;
     }

     public RoomType getRoomType() {
          return roomType;
     }

     public void setRoomType(RoomType roomType) {
          this.roomType = roomType;
     }

     public String getDescription() {
          return description;
     }

     public void setDescription(String description) {
          this.description = description;
     }

     public int getCapacity() {
          return capacity;
     }

     public void setCapacity(int capacity) {
          this.capacity = capacity;
     }
}