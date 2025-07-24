package com.onlineLibrary.payment.Flux;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.onlineLibrary.payment.Util.ConvertJsonUtils.jacksonToGson;

@Service
public class NotificationService implements INotificationService {


    private ProfilMicroservicesClient profilMicroservicesClient;
    private NotificationBuilder notificationBuilder;

    @Autowired
    public NotificationService(ProfilMicroservicesClient profilMicroservicesClient, NotificationBuilder notificationBuilder) {
        this.profilMicroservicesClient = profilMicroservicesClient;
        this.notificationBuilder = notificationBuilder;
    }


    @Override
    public JsonObject notifyUser(int userId, int orderId, int cardId,double totalPrice) throws Exception {
        ResponseEntity<JsonNode> responseJackson = profilMicroservicesClient.callGetUserData(userId);
        JsonObject notificationResult = jacksonToGson(responseJackson.getBody()).getAsJsonObject();
        return notificationBuilder.buildPaymentNotification(notificationResult,orderId,cardId,totalPrice);
    }
}


