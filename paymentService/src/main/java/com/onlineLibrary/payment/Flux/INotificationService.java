package com.onlineLibrary.payment.Flux;

import com.onlineLibrary.payment.Entities.DTO.NotificationResponseDTO;

public interface INotificationService {
    NotificationResponseDTO notifyUser(int userId, int orderId, int cartId, double totalPrice)throws Exception;
}
