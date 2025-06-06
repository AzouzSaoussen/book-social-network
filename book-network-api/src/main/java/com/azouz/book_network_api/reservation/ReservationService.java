package com.azouz.book_network_api.reservation;

import com.azouz.book_network_api.book.*;
import com.azouz.book_network_api.common.PageResponse;
import com.azouz.book_network_api.user.User;
import com.azouz.book_network_api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(ReservationService.class);
    private final BookRepository bookRepository;
    private final ReservationMapper reservationMapper;

    public Integer save(Integer bookId, Authentication connectedUser) {
        logger.info("Saving reservation for book {} to user {}", bookId, connectedUser.getName());
        User currentUser =(User) connectedUser.getPrincipal();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(()-> new RuntimeException("user mot found"));
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new RuntimeException("book not found"));
        return reservationRepository.save(
                Reservation.builder()
                        .book(book)
                        .user(user)
                        .build()
        ).getId();
    }

    public PageResponse<ReservedBookResponse> getReservedBooksByUser(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Reservation> reservations= reservationRepository.findAllByUser(pageable, user);
        List<ReservedBookResponse> borrowedBooksResponse= reservations
                .stream()
                .map(reservationMapper::toReservedBookResponse)
                .toList();
       return new PageResponse<>(
               borrowedBooksResponse,
               reservations.getNumber(),
               reservations.getSize(),
               reservations.getTotalElements(),
               reservations.getTotalPages(),
               reservations.isFirst(),
               reservations.isLast()
       );
    }
}
