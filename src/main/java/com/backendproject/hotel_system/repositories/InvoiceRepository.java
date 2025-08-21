package com.backendproject.hotel_system.repositories;

import com.backendproject.hotel_system.Models.Booking;
import com.backendproject.hotel_system.Models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
    Optional<Invoice> findByBookingId(long bookingId);

    List<Invoice> findByCustomerSessionId(Long customerSessionId);
}
