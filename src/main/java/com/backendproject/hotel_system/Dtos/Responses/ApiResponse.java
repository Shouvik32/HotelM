package com.backendproject.hotel_system.Dtos.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class  ApiResponse<T>{
    private String status;
    private String message;
    private T data;


}
