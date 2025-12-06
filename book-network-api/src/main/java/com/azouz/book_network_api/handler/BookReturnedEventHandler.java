package com.azouz.book_network_api.handler;

import com.azouz.book_network_api.book.BookReturnedEvent;
import com.azouz.book_network_api.notification.DelayedNotifier;
import com.azouz.book_network_api.notification.NotificationService;
import com.azouz.book_network_api.reservation.ReservationQueueService;
import com.azouz.book_network_api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookReturnedEventHandler {

    private final ReservationQueueService queueService;
    private final NotificationService notificationService;
    private final DelayedNotifier delayedNotifier;

    @EventListener
    public void handle(BookReturnedEvent event) {

        Integer bookId = event.bookId();

        // 🎯 immediate user
        User firstUser = queueService.processQueue(bookId);

        if (firstUser != null) {
            notificationService.notifyUser(firstUser.getId(),
                    "The book is now available!", bookId);

            // schedule next notification for 24h later
            delayedNotifier.scheduleNextNotification(bookId);
        }
    }
}

