package com.onlineLibrary.order.Flux.Interfaces;

import com.onlineLibrary.order.Entities.DAO.OrderLineDAO;
import com.onlineLibrary.order.Entities.DTO.DeliveryDTO;
import com.onlineLibrary.order.Entities.DTO.OrderResponseDTO;

import java.util.List;
import java.util.Optional;


public interface IOrderService {


    OrderResponseDTO placeOrder(int userId, boolean autoDelivery) throws Exception;
    Optional<List<OrderLineDAO>> deliveryOrder(int userId, int orderId);
    List<DeliveryDTO> displayAllOrders();
}
