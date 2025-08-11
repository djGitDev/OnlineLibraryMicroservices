package com.onlineLibrary.order.Flux.Implementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.onlineLibrary.order.Flux.Interfaces.ProfilMicroservicesClient;
import com.onlineLibrary.order.Persistance.Interfaces.IOrderLineRepository;
import com.google.gson.JsonObject;
import com.onlineLibrary.order.Flux.Interfaces.ISynchronizedOrderManager;
import com.onlineLibrary.order.Entities.Delivery;
import com.onlineLibrary.order.Flux.Interfaces.IDeliveryService;
import com.onlineLibrary.order.Persistance.Interfaces.IDeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.onlineLibrary.order.Util.ConvertJsonUtils.jacksonToGson;

@Service
public class DeliveryService implements IDeliveryService {

    private IOrderLineRepository orderLineRepository;
    private IDeliveryRepository deliveryRepository;
    private NotificationProducer notificationProducer;
    private ProfilMicroservicesClient profilMicroservicesClient;


    @Autowired
    public DeliveryService(IDeliveryRepository deliveryRepository,IOrderLineRepository orderLineRepository,NotificationProducer notificationProducer,ProfilMicroservicesClient profilMicroservicesClient) {
        this.deliveryRepository = deliveryRepository;
        this.orderLineRepository = orderLineRepository;
        this.notificationProducer = notificationProducer;
        this.profilMicroservicesClient = profilMicroservicesClient;
    }

    @Override
    public Delivery scheduleDelivery(int orderId, JsonObject adresse) throws Exception {
         Delivery delivery = new Delivery(orderId);
         delivery.setAdresse(adresse);
         int deliveryId = deliveryRepository.save(delivery);
         delivery.setId(deliveryId);
         return delivery;
    }

    @Override
    public JsonObject deliverOrder(int orderId) {
        orderLineRepository.updateDeliveryStatusToDelivered(orderId);
        return  deliveryRepository.deliverOrder(orderId);
    }

    @Override
    public JsonObject findDelivryByOrderId(int orderId) {
        return deliveryRepository.findDeliveryByOrderId(orderId);
    }

    @Override
    public void scheduleAutoDelivery(int userId, int orderId) {
        new Thread(() -> {
            try {
                Thread.sleep(10000);

                JsonObject deliveryReport = this.deliverOrder(orderId);
                ResponseEntity<JsonNode> respenseJackson = profilMicroservicesClient.callGetUserData(userId);
                JsonObject jsonUserProfil= jacksonToGson(respenseJackson.getBody()).getAsJsonObject();
                String email = jsonUserProfil.get("email").getAsString();
                deliveryReport.addProperty("email", email);
                notificationProducer.sendNotification(deliveryReport.toString());

                ISynchronizedOrderManager delayedResults = ISynchronizedOrderManager.init();
                synchronized (delayedResults) {
                    delayedResults.addRepport(deliveryReport);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
