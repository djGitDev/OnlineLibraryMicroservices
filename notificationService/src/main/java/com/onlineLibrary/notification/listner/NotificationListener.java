package com.onlineLibrary.notification.listner;

//import com.onlineLibrary.notification.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotificationListener {

//    private EmailService emailService;
private static final Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    private final ObjectMapper objectMapper;

    @Autowired
    public  NotificationListener(ObjectMapper objectMapper){
//        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }



    @KafkaListener(topics = "topic-notification", groupId = "notification-service-group")
    public void listen(String message) {
        try {
            logger.info("Received Kafka message zzzzzzzzzzzzz: {}", message);
//            JsonNode jsonNode = objectMapper.readTree(message);

//            emailService.sendEmail(email, "Notification Subject", "Your notification message");

//            System.out.println("Email sent to: " + email);
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            // Gérer les erreurs proprement (log, dead letter, etc)
        }
    }
}
