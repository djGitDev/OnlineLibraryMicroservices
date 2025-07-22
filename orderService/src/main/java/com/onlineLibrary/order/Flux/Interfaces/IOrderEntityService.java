package com.onlineLibrary.order.Flux.Interfaces;

import com.google.gson.JsonObject;

public interface IOrderEntityService {
    JsonObject createOrder(int userId, JsonObject jsonUserProfil) throws Exception;
    JsonObject deliverOrder(int orderId);
    JsonObject displayOrders();
    void scheduleAutoDelivery(int userId);
}
