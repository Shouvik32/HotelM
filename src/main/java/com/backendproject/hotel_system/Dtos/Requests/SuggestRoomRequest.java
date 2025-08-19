package com.backendproject.hotel_system.Dtos.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;


@Data
@AllArgsConstructor
public class SuggestRoomRequest {
    int noOfGuests;
    Date checkin;
    Date checkout;

}
