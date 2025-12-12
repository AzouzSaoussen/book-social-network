package com.azouz.book_network_api.notification;

import com.azouz.book_network_api.reservation.ReservationQueueService;
import com.azouz.book_network_api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DelayedNotifier {

    private final ReservationQueueService queueService;
    private final NotificationService notificationService;


    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public void scheduleNextNotification(Integer bookId, String bookTitle) {
        scheduler.schedule(() -> {
            User nextUser = queueService.getNextUserAfter(null, bookId);
            if (nextUser != null) {
                notificationService.notifyUser(nextUser.getId(),
                        "The book is now available (24h slot)!", bookId, bookTitle);
            }
        }, 24, TimeUnit.HOURS);
    }
}

