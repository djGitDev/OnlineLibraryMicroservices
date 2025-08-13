package com.onlineLibrary.order.Flux.Implementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.onlineLibrary.order.Entities.DAO.DeliveryDAO;
import com.onlineLibrary.order.Entities.DTO.NotificationResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class NotificationEventBuilder {


    public static NotificationResponseDTO buildDeliveryStatusChangeNotification(
            DeliveryDAO delivery,
            JsonNode userProfile
    ) {
        String email = userProfile.has("email") ? userProfile.get("email").asText() : "unknown@example.com";
        String subject = "Order Status Update";
        String message = String.format(
                "Hello,\n\n" +
                        "Your order (ID: %d) status has changed to: %s.\n\n" +
                        "Thank you for your trust.\n\n",
                delivery.getOrderId(),
                delivery.getStatut()
        );

        return new NotificationResponseDTO(email, subject, message);
    }
}
