package com.backendproject.hotel_system.Dtos.Responses;

import com.backendproject.hotel_system.Models.User;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String userRole;
    private String phone;
    private String address;
    private String city;
    private String state;
    private int zip;

    public static UserResponse from(User user) {
        UserResponse dto=new UserResponse();
        dto.setUserId( user.getId()) ;
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setUserRole(user.getUserRole());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setState(user.getState());
        dto.setZip(user.getZip());
        return dto;
    }

}
//token ---   >         eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiU2hvdXZpayBEYXMiLCJlbWFpbCI6ImRhcy5zaG91dmlrMDFAZ21haWwuY29tIiwiZGF0ZSI6IjIwMjUtMDYtMTMgMTk6NDA6MTEifQ.dx6zfJiAUTK89kxanNAUnQezyrcuuQzWXYHcUlVO5sg



//eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiU2hvdXZpayBEYXMiLCJlbWFpbCI6ImRhcy5zaG91dmlrMDFAZ21haWwuY29tIiwiZGF0ZSI6IjIwMjUtMDYtMTMgMTk6NDI6MjYifQ.V3LvTm4jMrIXGHgoshi_xseGhQf3Y2IgPT6TlsPclRg

