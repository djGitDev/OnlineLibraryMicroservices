package com.onlineLibrary.order.Flux.Implementations;

import com.google.gson.JsonObject;
import com.onlineLibrary.order.Entities.CartItem;
import com.onlineLibrary.order.Entities.OrderLine;
import com.onlineLibrary.order.Flux.Interfaces.IOrderLineService;
import com.onlineLibrary.order.Persistance.Interfaces.IOrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderLineService implements IOrderLineService {

    private IOrderLineRepository orderLineRepository;

    @Autowired
    public OrderLineService(IOrderLineRepository orderLineRepository) {
        this.orderLineRepository = orderLineRepository;
    }


    @Override
    public List<OrderLine> convertCartItemsToOrderLines(List<CartItem> cartItems, int orderId) throws Exception {
        List<OrderLine> orderLines = new ArrayList<>();

        for (CartItem item : cartItems) {
            OrderLine orderLine = new OrderLine(orderId,item.getBookId(),item.getQuantity());
            orderLine.setStatutLivraison("En attente de livraison");
            orderLine.setDateLivraisonEffective(LocalDateTime.now().plusSeconds(10));
            int orderLineId = orderLineRepository.save(orderId,orderLine);
            orderLine.setId(orderLineId);
            orderLines.add(orderLine);
        }

        return orderLines;    }

    @Override
    public JsonObject markAsDelivred(int orderId) {
        return orderLineRepository.markAsDelivred(orderId);
    }
}
