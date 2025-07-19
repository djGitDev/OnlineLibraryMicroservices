package com.example.Persistance.Interfaces;

import com.google.gson.JsonObject;
import com.example.Entities.Delivery;

public interface IDeliveryRepository {
    int save(Delivery delivery) throws Exception;
    JsonObject deliverOrder(int orderId);
    JsonObject findDeliveryByOrderId(int orderId);

}
