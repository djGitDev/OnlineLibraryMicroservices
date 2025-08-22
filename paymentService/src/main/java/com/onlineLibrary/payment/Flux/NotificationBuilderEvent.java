package com.onlineLibrary.payment.Flux;

import com.fasterxml.jackson.databind.JsonNode;
import com.onlineLibrary.payment.Entities.DTO.NotificationResponseDTO;

public class NotificationBuilderEvent {

    public static NotificationResponseDTO buildPaymentNotification(JsonNode notificationResult, int orderId, int cartId, double totalPrice) {

        String firstName = notificationResult.get("first_name").asText();
        String email = notificationResult.get("email").asText();

        String subject = "Payment Confirmation";
        String message = String.format(
                "Hello %s,\n\n" +
                        "We confirm that a payment of $%.2f has been successfully made for your cart (ID: %d).\n" +
                        "Your order (ID: %d) has been triggered and is being processed.\n\n" +
                        "Thank you for your trust.\n\n",
                firstName, totalPrice, cartId, orderId
        );

        return new NotificationResponseDTO(email, subject, message);
    }



}
