package com.example.Persistance.Interfaces;

import com.google.gson.JsonObject;
import com.example.Entities.Order;

public interface IOrderEntityRepository {
    int save(Order order) throws Exception;
    JsonObject fetchAllOrders();
    int getOrderByUserId(int userId);
}
