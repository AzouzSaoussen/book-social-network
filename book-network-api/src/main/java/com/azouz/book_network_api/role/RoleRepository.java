package com.azouz.book_network_api.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Integer;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String role);
}
