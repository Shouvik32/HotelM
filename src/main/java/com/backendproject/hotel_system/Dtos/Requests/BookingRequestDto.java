package com.backendproject.hotel_system.Dtos.Requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {

    private Long userId;
    private long hotelId;
    private List<Long> roomIds;
    private double serviceCharge;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkIn;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkOut;
}
