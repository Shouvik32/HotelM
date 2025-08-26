package com.backendproject.hotel_system.Dtos.Responses;

import com.backendproject.hotel_system.Models.Booking;
import com.backendproject.hotel_system.Models.BookingRoom;
import com.backendproject.hotel_system.Models.Invoice;
import com.backendproject.hotel_system.Models.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private long bookingId;
    private String userName;
    private String phone;
    private String address;
    private String hotelName;
    private List<String> rooms;
    private double totalAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkIn;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkOut;

    public static BookingResponse fromInvoice(Invoice invoice, Date checkin, Date checkout) {
        if (invoice == null || invoice.getBookings() == null || invoice.getBookings().isEmpty()) {
            return null;
        }

        // Take customer directly from invoice
        User user = invoice.getCustomer();
        String hotelName = invoice.getHotel() != null ? invoice.getHotel().getName() : "-";

        // Collect all room numbers across all bookings
        List<String> roomNumbers = invoice.getBookings().stream()
                .flatMap(b -> b.getBookingRooms().stream())
                .map(br -> br.getRoom().getRoomNumber())
                .toList();

        BookingResponse response = new BookingResponse();
        response.setBookingId(invoice.getId()); // Use Invoice ID, not Booking ID anymore
        response.setUserName(user.getFirstName() + " " + user.getLastName());
        response.setPhone(user.getPhone());
        response.setAddress(user.getAddress());
        response.setHotelName(hotelName);
        response.setRooms(roomNumbers);
        response.setTotalAmount(invoice.getTotalAmount());
        response.setCheckIn(checkin != null ? checkin : invoice.getCheckInDate());
        response.setCheckOut(checkout != null ? checkout : invoice.getCheckOutDate());

        return response;
    }
    public static BookingResponse from(Booking booking){
        BookingResponse response = new BookingResponse();

        response.setBookingId(booking.getId());
        response.setUserName(booking.getCustomerSession() != null ? booking.getCustomerSession().getUser().getFirstName() : null);
        response.setPhone(booking.getCustomerSession() != null ? booking.getCustomerSession().getUser().getPhone() : null);
        response.setAddress(booking.getCustomerSession() != null ? booking.getCustomerSession().getUser().getAddress() : null);
        response.setHotelName(booking.getHotel() != null ? booking.getHotel().getName() : null);
        if (booking.getBookingRooms() != null) {
            List<String> roomNames = booking.getBookingRooms()
                    .stream()
                    .map(room -> room.getRoom().getRoomNumber())
                    .toList();
            response.setRooms(roomNames);
        }
        response.setCheckIn(booking.getCheckInDate());
        response.setCheckOut(booking.getCheckOutDate());

        // Set total amount (assuming you have a method to calculate it)
        response.setTotalAmount(
                booking.getBookingRooms() != null
                        ? booking.getBookingRooms()
                        .stream()
                        .mapToDouble(br -> br.getRoom().getPrice())
                        .sum()
                        : 0.0
        );

        return response;
    }
    }



