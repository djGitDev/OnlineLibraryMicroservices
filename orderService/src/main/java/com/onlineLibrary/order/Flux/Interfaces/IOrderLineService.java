package com.onlineLibrary.order.Flux.Interfaces;

import com.google.gson.JsonObject;
import com.onlineLibrary.order.Entities.CartItem;
import com.onlineLibrary.order.Entities.OrderLine;

import java.util.List;

public interface IOrderLineService {
    List<OrderLine> convertCartItemsToOrderLines(List<CartItem> cartItems, int orderId) throws Exception;
    JsonObject markAsDelivred(int orderId);
}

