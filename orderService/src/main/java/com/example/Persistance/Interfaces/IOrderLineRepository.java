package com.example.Persistance.Interfaces;

import com.google.gson.JsonObject;
import com.example.Entities.OrderLine;

public interface IOrderLineRepository {
    int save(int orderId, OrderLine orderLine) throws Exception;
    JsonObject markAsDelivred(int orderId);
}
