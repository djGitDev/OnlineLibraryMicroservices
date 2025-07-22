package com.onlineLibrary.order.Persistance.Interfaces;

import com.google.gson.JsonObject;
import com.onlineLibrary.order.Entities.OrderLine;

public interface IOrderLineRepository {
    int save(int orderId, OrderLine orderLine) throws Exception;
    JsonObject markAsDelivred(int orderId);
    JsonObject updateDeliveryStatusToDelivered(int orderId);
}
