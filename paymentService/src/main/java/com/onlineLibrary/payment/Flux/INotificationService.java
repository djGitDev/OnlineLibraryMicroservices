package com.onlineLibrary.payment.Flux;

import com.google.gson.JsonObject;

public interface INotificationService {
    JsonObject notifyUser(int userId, int orderId, int cartId, double totalPrice)throws Exception;
}
