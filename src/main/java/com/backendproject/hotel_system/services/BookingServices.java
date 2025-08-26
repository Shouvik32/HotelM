package com.backendproject.hotel_system.services;

import com.backendproject.hotel_system.Models.Booking;
import com.backendproject.hotel_system.Models.Invoice;
import com.backendproject.hotel_system.Models.Room;
import com.backendproject.hotel_system.Strategies.BookingExpenses.StrategyType;

import java.util.Date;
import java.util.List;

public interface BookingServices {

  public Invoice bookRoom(Long customerSessionId, List<Long> roomIds, Date checkIn, Date checkOut, StrategyType type);
    public Invoice getInvoiceByBookingId(Long bookingId);

    Booking getBookingById(Long bookingId);

    Invoice getInvoiceById(Long invoiceId);
}
