package com.onlineLibrary.order.Persistance.Interfaces;

import com.google.gson.JsonObject;
import com.onlineLibrary.order.Entities.Order;

public interface IOrderEntityRepository {
    int save(Order order) throws Exception;
    JsonObject fetchAllOrders();
    int getOrderByUserId(int userId);
}
