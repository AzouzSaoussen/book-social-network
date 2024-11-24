package com.azouz.book_network_api.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Integer;

public interface UserRepository extends JpaRepository <User, Integer>{
    Optional<User> findByEmail(String email);
}
