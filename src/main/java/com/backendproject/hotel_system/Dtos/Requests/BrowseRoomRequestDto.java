package com.backendproject.hotel_system.Dtos.Requests;

import com.backendproject.hotel_system.Models.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


@Data
public class BrowseRoomRequestDto {
    private RoomType roomType;
    private Double price;
    private Integer capacity;

    @Getter
    @AllArgsConstructor

    public static class UserloginRequest {
        String email;
        String password;
    }
}
