package com.backendproject.hotel_system.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;


    @Entity
    @Data
    @Table(name = "roles")
    public class RolesUser extends BaseModel {
        @Column(unique = true)
        private String name;
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "role_permissions",
                joinColumns = @JoinColumn(name = "role_id"),
                inverseJoinColumns = @JoinColumn(name = "permission_id")
        )

        private Set<Permission> permissions;
    }



