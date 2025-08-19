package com.backendproject.hotel_system.repositories;

import com.backendproject.hotel_system.Models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    public Location save(Location location);
    public Location getLocationById(Long id);
}
