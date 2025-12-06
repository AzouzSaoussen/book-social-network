package com.azouz.book_network_api.reservation;

import com.azouz.book_network_api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationQueueService {
    private final ReservationRepository reservationRepository;
    /*
      Processes the queue when a returned book becomes available.
      Returns the user who should be notified.
     */
    public User processQueue(Integer bookId){
        List<Reservation> queue = reservationRepository.findByBookIdOrderByQueuePositionAsc(bookId);
        if (queue.isEmpty()) return null;
        // User at position 0
        Reservation first = queue.get(0);
        User nextUser = first.getUser();

        // Remove first from queue
        reservationRepository.delete(first);

        // Shift queue positions
        for (int i = 1; i < queue.size(); i++) {
            queue.get(i).setQueuePosition(i - 1);
        }
        reservationRepository.saveAll(queue.subList(1, queue.size()));

        return nextUser;

    }
    public User getNextUserAfter(User current, Integer bookId) {
        return reservationRepository
                .findByBookIdOrderByQueuePositionAsc(bookId)
                .stream()
                .findFirst()
                .map(Reservation::getUser)
                .orElse(null);
    }


}
