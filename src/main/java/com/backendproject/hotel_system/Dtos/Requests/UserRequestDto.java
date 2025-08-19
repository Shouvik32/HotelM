package com.backendproject.hotel_system.Dtos.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;


@Data
@AllArgsConstructor
public class UserRequestDto {

    private String firstName;
    private String lastName;
    private String email;
    private  String password;
    private String userRole;
    private String phone;
    private String address;
    private String city;
    private String state;
    private int zip;


}
