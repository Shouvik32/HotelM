package com.backendproject.hotel_system.Dtos.Responses;

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

    public static BookingResponse fromInvoice(com.backendproject.hotel_system.Models.Invoice invoice, Date checkin,Date checkout) {
        if (invoice == null || invoice.getBookingRooms() == null || invoice.getBookingRooms().isEmpty()) {
            return null;
        }
        com.backendproject.hotel_system.Models.User user =
                invoice.getCustomerSession().getUser();
        String hotelName = invoice.getBookingRooms().get(0).getRoom().getHotel().getName();
        List<String> roomNumbers = invoice.getBookingRooms().stream()
                .map(br -> br.getRoom().getRoomNumber())
                .toList();

        BookingResponse response = new BookingResponse();
        response.setBookingId(invoice.getBookingId());
        response.setUserName(user.getFirstName() + " " + user.getLastName());
        response.setPhone(user.getPhone());
        response.setAddress(user.getAddress());
        response.setHotelName(hotelName);
        response.setRooms(roomNumbers);
        response.setTotalAmount(invoice.getTotalAmount());
        response.setCheckIn(checkin);
        response.setCheckOut(checkout);
        return response;
    }
    }



