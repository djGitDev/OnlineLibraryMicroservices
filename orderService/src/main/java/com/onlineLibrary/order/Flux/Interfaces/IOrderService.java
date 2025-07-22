package com.onlineLibrary.order.Flux.Interfaces;

import com.google.gson.JsonObject;

import java.util.Map;

public interface IOrderService {
    

    JsonObject placeOrder(int userId,boolean autoDelivery) throws Exception;
    JsonObject deliveryOrder(int orderId);
    JsonObject displayAllOrders();
    JsonObject displayOrders();
}
