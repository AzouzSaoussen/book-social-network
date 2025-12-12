package com.azouz.book_network_api.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyUser(Integer userId, String message, Integer bookId, String bookTitle) {
        messagingTemplate.convertAndSend("/topic/user/" + userId,
                new NotificationMessage(message, bookId, bookTitle));
    }
}

