package com.azouz.book_network_api.reservation;

import com.azouz.book_network_api.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Page<Reservation> findAllByUser(Pageable pageable, User user);
}
