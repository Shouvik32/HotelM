package com.backendproject.hotel_system.services;

import com.backendproject.hotel_system.Models.Permission;
import com.backendproject.hotel_system.Models.RolesUser;
import com.backendproject.hotel_system.repositories.RoleRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolewisePermissions {

    private final RoleRepository roleRepository;

    public RolewisePermissions(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Set<String> getPermissionsForRole(String roleName) {
        return roleRepository.findByName(roleName.toUpperCase())
                .map(RolesUser::getPermissions)
                .orElse(Set.of())
                .stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }

    public Set<SimpleGrantedAuthority> getAuthoritiesForRole(String roleName) {
        return getPermissionsForRole(roleName).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
