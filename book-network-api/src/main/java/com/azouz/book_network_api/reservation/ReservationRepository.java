package com.azouz.book_network_api.reservation;

import com.azouz.book_network_api.book.Book;
import com.azouz.book_network_api.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Page<Reservation> findAllByUser(Pageable pageable, User user);

    List<Reservation> findByBookOrderByQueuePositionAsc(Book book);

    @Query("SELECT r.queuePosition FROM Reservation r WHERE r.book = :book ORDER BY r.queuePosition DESC")
    List<Integer> findQueuePositionsDesc(@Param("book") Book book);

    List<Reservation> findByBookIdOrderByQueuePositionAsc(Integer bookId);
}
