package com.backendproject.hotel_system.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@Entity
public class Rating extends BaseModel {
    @ManyToOne
     Hotel hotel;
     double ratingValue;

     private long userId;


}
