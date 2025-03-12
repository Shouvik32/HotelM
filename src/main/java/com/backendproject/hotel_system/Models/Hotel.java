package com.backendproject.hotel_system.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Data
@NoArgsConstructor
@Entity
public class Hotel extends BaseModel{

     private String name;
     private HotelStars stars;
     @ManyToOne
     private Rating rating;


}
