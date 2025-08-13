package com.onlineLibrary.order.Flux.Implementations;


import com.onlineLibrary.order.Entities.DAO.OrderLineDAO;
import com.onlineLibrary.order.Entities.DTO.ItemDTO;
import com.onlineLibrary.order.Flux.Interfaces.IOrderLineService;
import com.onlineLibrary.order.Persistance.IOrderLineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderLineService implements IOrderLineService {
    private static final Logger logger = LoggerFactory.getLogger(OrderLineService.class);

    private IOrderLineRepository orderLineRepository;

    @Autowired
    public OrderLineService(IOrderLineRepository orderLineRepository) {
        this.orderLineRepository = orderLineRepository;
    }


    @Override
    public List<OrderLineDAO> convertCartItemsToOrderLines(List<ItemDTO> Items, int orderId) throws Exception {
        List<OrderLineDAO> orderLines = new ArrayList<>();
        for (ItemDTO item : Items) {
            int bookId = item.getBookId();
            int quantity = item.getQuantity();
            OrderLineDAO orderLine = new OrderLineDAO(orderId,bookId,quantity);
            orderLine.setDeliveryStatut("PENDING DELIVERY");
            orderLine = orderLineRepository.save(orderLine);
            orderLines.add(orderLine);
        }

        return orderLines;
    }

    @Override
    public Optional<List<OrderLineDAO>> markAsDelivred(int orderId) {
        orderLineRepository.markAsDeliveredByOrderId(orderId);
        return orderLineRepository.findByOrderId(orderId);
    }
}
