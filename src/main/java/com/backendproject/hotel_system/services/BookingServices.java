package com.backendproject.hotel_system.services;

import com.backendproject.hotel_system.Models.Invoice;
import com.backendproject.hotel_system.Models.Room;

import java.util.Date;
import java.util.List;

public interface BookingServices {

  public Invoice bookRoom(Long customerSessionId, List<Long> roomIds, Date checkIn, Date checkOut);
    public Invoice getInvoiceByBookingId(Long bookingId);
}
