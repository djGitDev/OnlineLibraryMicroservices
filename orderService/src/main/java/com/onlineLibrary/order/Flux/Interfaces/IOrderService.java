package com.onlineLibrary.order.Flux.Interfaces;

import com.google.gson.JsonObject;

import java.util.Map;

public interface IOrderService {
    
    JsonObject clearCart(int userId) throws Exception;
    JsonObject clearBooks(int userId, JsonObject objectElement);
    JsonObject placeOrder(int userId,boolean autoDelivery) throws Exception;
    JsonObject deliveryOrder(int orderId);
    JsonObject displayAllOrders();
    JsonObject addSearchedItemsToCart(int lastUserId, JsonObject jsonObject, Map<Integer,Double> searchedBooksIds) throws Exception;
    JsonObject getTotalPriceCart(int cartId) throws Exception;
    JsonObject displayOrders();
}
