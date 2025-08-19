package com.backendproject.hotel_system.repositories;

import com.backendproject.hotel_system.Models.RolesUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository  extends JpaRepository<RolesUser, Long> {
    Optional<RolesUser> findByName(String name);


}


