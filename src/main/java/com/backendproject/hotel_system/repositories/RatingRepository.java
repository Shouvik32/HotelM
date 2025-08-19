package com.backendproject.hotel_system.repositories;

import com.backendproject.hotel_system.Models.Hotel;
import com.backendproject.hotel_system.Models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
   // public Rating addRating(Rating rating, Hotel hotel);
    //public double getAverageRating(Hotel hotel);
}
