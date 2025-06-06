package com.azouz.book_network_api.reservation;

import com.azouz.book_network_api.history.BookTransactionHistory;
import com.azouz.book_network_api.history.BookTransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class ReservationMapper {
private final Logger logger = LoggerFactory.getLogger(ReservationMapper.class);
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    public ReservedBookResponse toReservedBookResponse(Reservation reservation) {
        BookTransactionHistory history = bookTransactionHistoryRepository.findFirstByBookIdOrderByCreatedDateDesc(reservation.getBook().getId()).orElseThrow(()-> new RuntimeException("Book transaction history not found"));
        logger.info("Book transaction history returning state: {}", history.isReturnApproved());
        return ReservedBookResponse.builder()
                .id(reservation.getBook().getId())
                .title(reservation.getBook().getTitle())
                .authorName(reservation.getBook().getAuthorName())
                .isbn(reservation.getBook().getIsbn())
                .rate(reservation.getBook().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}
