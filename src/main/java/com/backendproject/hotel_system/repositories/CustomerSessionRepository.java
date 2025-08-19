package com.backendproject.hotel_system.repositories;

import com.backendproject.hotel_system.Models.CustomerSession;
import com.backendproject.hotel_system.Models.CustomerSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CustomerSessionRepository  extends JpaRepository<CustomerSession,Long> {
    Optional<CustomerSession> findFirstByUser_IdAndCustomerSessionStatus(Long userId, CustomerSessionStatus customerSessionStatus);
}
