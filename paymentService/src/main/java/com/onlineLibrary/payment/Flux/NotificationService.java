package com.onlineLibrary.payment.Flux;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineLibrary.payment.Entities.DTO.NotificationResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class NotificationService implements INotificationService {


    private final ProfilMicroservicesClient profilMicroservicesClient;
    private final NotificationBuilderEvent notificationBuilderEvent;
    private final NotificationProducer notificationProducer;
    private final ObjectMapper objectMapper;

    @Autowired
    public NotificationService(ProfilMicroservicesClient profilMicroservicesClient, NotificationBuilderEvent notificationBuilderEvent, NotificationProducer notificationProducer,ObjectMapper objectMapper) {
        this.profilMicroservicesClient = profilMicroservicesClient;
        this.notificationBuilderEvent = notificationBuilderEvent;
        this.notificationProducer = notificationProducer;
        this.objectMapper = objectMapper;
    }


    @Override
    public NotificationResponseDTO notifyUser(int userId, int orderId,int cardId,double totalPrice) throws Exception {
        ResponseEntity<JsonNode> responseJackson = profilMicroservicesClient.callGetUserData(userId);
        JsonNode dataUser = responseJackson.getBody();
        NotificationResponseDTO notificationEvent = notificationBuilderEvent.buildPaymentNotification(
                dataUser,
                orderId,
                cardId,
                totalPrice
        );
        JsonNode notificationEventJson = objectMapper.valueToTree(notificationEvent);
        notificationProducer.sendNotification(notificationEventJson.toString());
        return notificationEvent;
    }
}


