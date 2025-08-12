package com.onlineLibrary.payment.Flux;

import com.fasterxml.jackson.databind.JsonNode;
import com.onlineLibrary.payment.Entities.DTO.NotificationResponseDTO;

public class NotificationBuilderEvent {

public static NotificationResponseDTO buildPaymentNotification(JsonNode notificationResult, int orderId, int cartId, double totalPrice) {

    String firstName = notificationResult.get("first_name").asText();
    String email = notificationResult.get("email").asText();

    String subject = "Confirmation de paiement";
    String message = String.format(
            "Bonjour %s,\n\n" +
                    "Nous vous confirmons que le paiement de %.2f$ a été effectué avec succès pour votre panier (ID : %d).\n" +
                    "Votre commande (ID : %d) a été déclenchée et est en cours de traitement.\n\n" +
                    "Merci pour votre confiance.\n\n",
            firstName, totalPrice, cartId, orderId
    );
    
    return new NotificationResponseDTO(email, subject, message);
}



}
