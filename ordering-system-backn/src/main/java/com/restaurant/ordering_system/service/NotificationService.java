package com.restaurant.ordering_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    private final SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    public void sendKitchenNotification(String message) {
        messagingTemplate.convertAndSend("/topic/kitchen-orders", message);
    }
    
    public void sendWaiterNotification(String message) {
        messagingTemplate.convertAndSend("/topic/waiter-notifications", message);
    }
}
