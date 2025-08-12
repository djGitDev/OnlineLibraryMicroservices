package com.onlineLibrary.payment.Flux;

import com.onlineLibrary.payment.Entities.DTO.NotificationResponseDTO;


public interface IPaymentService {

    NotificationResponseDTO processPaiement(int cartId, int userId) throws Exception;
}
