package com.azouz.book_network_api.reservation;

import com.azouz.book_network_api.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reservations")
@RequiredArgsConstructor
@Tag(name= "Reservation")
public class ReservationController {
    private final ReservationService service;
    @PostMapping("/{book-id}")
    public ResponseEntity<Integer> save(@PathVariable("book-id") Integer bookId, Authentication connectedUser) {
        return ResponseEntity.ok(service.save(bookId, connectedUser));
    }
    @GetMapping
    public ResponseEntity<PageResponse<ReservedBookResponse>>getReservedBooksByUser(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name ="size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.getReservedBooksByUser(page, size, connectedUser));
    }
}
