package com.example.Flux.Interfaces;

import com.google.gson.JsonObject;
import com.example.Entities.CartItem;
import com.example.Entities.OrderLine;

import java.util.List;

public interface IOrderLineService {
    List<OrderLine> convertCartItemsToOrderLines(List<CartItem> cartItems, int orderId) throws Exception;
    JsonObject markAsDelivred(int orderId);
}

