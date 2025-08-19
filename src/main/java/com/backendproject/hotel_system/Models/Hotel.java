package com.backendproject.hotel_system.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Data
@NoArgsConstructor
@Entity
public class Hotel extends BaseModel{

     private String name;
     @Enumerated(EnumType.STRING)
     @Column(name = "stars")
     private HotelStars stars;
     @ManyToOne
     private Rating rating;
     @ManyToOne
     private Location location;
     @OneToMany(mappedBy = "hotel",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
     public List<Room> rooms = new ArrayList<>();
}
