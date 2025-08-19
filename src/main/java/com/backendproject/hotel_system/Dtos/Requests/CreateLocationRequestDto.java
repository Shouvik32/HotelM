package com.backendproject.hotel_system.Dtos.Requests;


import lombok.Data;


@Data
public class CreateLocationRequestDto {
    private String address;
    private String city;
    private String country;
    private String state;
    private String zip;
    private double latitude;
    private double longitude;

}
