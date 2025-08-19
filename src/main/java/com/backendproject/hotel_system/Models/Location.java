package com.backendproject.hotel_system.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity

public class Location extends BaseModel {
    private String address;
    private String city;
    private String country;
    private String state;
    private String zip;
    private double latitude;
    private double longitude;

}
