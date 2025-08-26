package com.backendproject.hotel_system.repositories;

import com.backendproject.hotel_system.Models.Booking;
import com.backendproject.hotel_system.Models.Hotel;
import com.backendproject.hotel_system.Models.Invoice;
import com.backendproject.hotel_system.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByBookings_Id(Long bookingId);

    @Query("select distinct i from Invoice i join i.bookings b where b.customerSession.id = :customerSessionId")
    List<Invoice> findByCustomerSessionId(@Param("customerSessionId") Long customerSessionId);

    Optional<Invoice> findByCustomerAndHotelAndCheckInDateAndCheckOutDate(
            User customer, Hotel hotel, Date checkInDate, Date checkOutDate
    );

    @Query("select i from Invoice i " +
            "where i.customer.id = :customerId and i.hotel.id = :hotelId " +
            "and i.checkInDate = :checkInDate and i.checkOutDate = :checkOutDate")
    Optional<Invoice> findExistingInvoice(@Param("customerId") Long customerId,
                                          @Param("hotelId") Long hotelId,
                                          @Param("checkInDate") Date checkInDate,
                                          @Param("checkOutDate") Date checkOutDate);
}
