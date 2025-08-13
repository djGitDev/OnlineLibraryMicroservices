package com.onlineLibrary.order.Flux.Interfaces;

import com.onlineLibrary.order.Entities.DAO.OrderLineDAO;
import com.onlineLibrary.order.Entities.DTO.AddressDTO;
import com.onlineLibrary.order.Entities.DTO.DeliveryDTO;
import com.onlineLibrary.order.Entities.DTO.OrderResponseDTO;

import java.util.List;
import java.util.Optional;

public interface IOrderEntityService {
    OrderResponseDTO createOrder(int userId, AddressDTO UserProfil) throws Exception;
    Optional<List<OrderLineDAO>> deliverOrder(int userId, int orderId);
    List<DeliveryDTO> displayOrders();
    void scheduleAutoDelivery(int userId, int orderId);
}
