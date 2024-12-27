package com.health.houseOfNature.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketNotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void broadcastUpdate(Object update) {
        messagingTemplate.convertAndSend("/topic/updates", update);
    }

    public void broadcastDeletion(Long doctorId) {
        messagingTemplate.convertAndSend("/topic/doctor-deletions", doctorId);
    }
}
