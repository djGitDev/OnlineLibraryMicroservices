package com.onlineLibrary.order.Flux.Interfaces;

import com.onlineLibrary.order.Entities.DAO.OrderLineDAO;
import com.onlineLibrary.order.Entities.DTO.ItemDTO;

import java.util.List;
import java.util.Optional;

public interface IOrderLineService {
    List<OrderLineDAO> convertCartItemsToOrderLines(List<ItemDTO> Items, int orderId) throws Exception;
    Optional<List<OrderLineDAO>> markAsDelivred(int orderId);
}

