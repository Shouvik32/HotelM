package com.backendproject.hotel_system.repositories;

import com.backendproject.hotel_system.Models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUseridAndHasExpiredFalse(long userid);
    Optional<Token> findByToken(String token);
    Optional<Token> findTopByUseridAndHasExpiredFalseOrderByCreatedAtDesc(Long userid);
}
