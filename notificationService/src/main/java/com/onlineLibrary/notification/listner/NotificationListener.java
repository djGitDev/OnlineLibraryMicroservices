package com.onlineLibrary.notification.listner;

import com.onlineLibrary.notification.Flux.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotificationListener {


    private static final Logger logger = LoggerFactory.getLogger(NotificationListener.class);
    private EmailService emailService;

    private final ObjectMapper objectMapper;

    @Autowired
    public NotificationListener(ObjectMapper objectMapper, EmailService emailService) {
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }


    @KafkaListener(topics = "topic-notification-payment", groupId = "notification-service-group")
    public void listenPayment(String message) {
        processNotificationMessage(message, "Payment done notification");
    }

    @KafkaListener(topics = "topic-notification-delivery", groupId = "notification-service-group")
    public void listenDelivery(String message) {
        processNotificationMessage(message, "Delivery done notification");
    }

    private void processNotificationMessage(String message, String logPrefix) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String email = jsonNode.get("to").asText();
            String subject = jsonNode.get("subject").asText();
            String messageToSend = jsonNode.get("message").asText();

            emailService.sendEmail(email, subject, messageToSend);
            logger.info("{} sent to email: {}", logPrefix, email);
        } catch (Exception e) {
            logger.error("Error processing message: {}", e.getMessage());
        }
    }
}