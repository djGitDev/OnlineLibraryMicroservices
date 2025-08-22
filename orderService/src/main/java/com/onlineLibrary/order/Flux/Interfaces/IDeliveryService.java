package com.onlineLibrary.order.Flux.Interfaces;

import com.onlineLibrary.order.Entities.DTO.AddressDTO;
import com.onlineLibrary.order.Entities.DTO.DeliveryDTO;

import java.util.Optional;

public interface IDeliveryService {
    DeliveryDTO scheduleDelivery(int orderId, AddressDTO adresse) throws Exception;
    void deliverOrder(int userId, int orderId );
    Optional<DeliveryDTO> findDelivryByOrderId(int orderId);
    void scheduleAutoDelivery(int userId, int orderId);
}



