package com.backendproject.hotel_system.repositories;

import com.backendproject.hotel_system.Models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

}
