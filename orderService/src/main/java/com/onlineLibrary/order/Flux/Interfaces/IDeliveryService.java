package com.onlineLibrary.order.Flux.Interfaces;

import com.google.gson.JsonObject;
import com.onlineLibrary.order.Entities.Delivery;

public interface IDeliveryService {
    Delivery planifierLivraison(int orderId, JsonObject adresse) throws Exception;
    JsonObject deliverOrder(int orderId);
    JsonObject findDelivryByOrderId(int orderId);
    void scheduleAutoDelivery(int orderId);
}
