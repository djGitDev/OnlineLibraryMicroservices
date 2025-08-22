package com.onlineLibrary.order.Flux.Implementations;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineLibrary.order.Entities.DAO.OrderDAO;
import com.onlineLibrary.order.Entities.DAO.OrderLineDAO;
import com.onlineLibrary.order.Entities.DTO.AddressDTO;
import com.onlineLibrary.order.Entities.DTO.DeliveryDTO;
import com.onlineLibrary.order.Entities.DTO.ItemDTO;
import com.onlineLibrary.order.Entities.DTO.OrderResponseDTO;
import com.onlineLibrary.order.Flux.Interfaces.*;
import com.onlineLibrary.order.Persistance.IOrderEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Service
public class OrderEntityService implements IOrderEntityService {

    private static final Logger logger = LoggerFactory.getLogger(OrderEntityService.class);


    private InventaryMicroservicesClient inventaryMicroserviceClient;
    private CartMicroservicesClient cartMicroservicesClient;

    private IOrderLineService orderLineService;
    private IDeliveryService deliveryService;
    private IOrderEntityRepository orderEntityRepository;

    @Autowired
    public OrderEntityService
            (InventaryMicroservicesClient inventaryMicroserviceClient,
             CartMicroservicesClient cartMicroservicesClient,
             IOrderLineService orderLineService,
             IDeliveryService deliveryService,
             IOrderEntityRepository orderEntityRepository
            ) {
        this.inventaryMicroserviceClient = inventaryMicroserviceClient;
        this.cartMicroservicesClient = cartMicroservicesClient;
        this.orderLineService = orderLineService;
        this.deliveryService = deliveryService;
        this.orderEntityRepository = orderEntityRepository;
    }


    @Override
    public OrderResponseDTO createOrder(int userId, AddressDTO UserProfil) throws Exception {

        ResponseEntity<JsonNode> cartJackson = cartMicroservicesClient.callGetCart(userId);
        JsonNode responseCart = cartJackson.getBody();
        ResponseEntity<JsonNode> cartItemsJackson =
                cartMicroservicesClient.callGetItems(responseCart.get("cartId").asInt());
        JsonNode responseCartItems = cartItemsJackson.getBody();
        JsonNode itemsArray = responseCartItems.get("items");
        List<ItemDTO> itemsDtoList = mapItemsJsonNodeToDtoList(itemsArray);

        for (ItemDTO item : itemsDtoList) {
            int bookId = item.getBookId();
            int quantity = item.getQuantity();

            inventaryMicroserviceClient.callDecreaseBookQuantity(bookId, quantity);
        }

        // create order
        OrderDAO order = new OrderDAO(userId);
        order = orderEntityRepository.save(order);
        List<OrderLineDAO> orderLines = orderLineService.convertCartItemsToOrderLines(itemsDtoList, order.getId());
        order.setLignes(orderLines);

        // plan delivery
        DeliveryDTO delivery = deliveryService.scheduleDelivery(order.getId(), UserProfil);

        // clean cart
        ResponseEntity<JsonNode> responseCartClearedInfoJackson = cartMicroservicesClient.callClearCart(userId);
        JsonNode cartClearedInfo = responseCartClearedInfoJackson.getBody();

        OrderResponseDTO response = new OrderResponseDTO(order.getId(), delivery.getId(), cartClearedInfo);

        return response;

    }

    @Override
    public Optional<List<OrderLineDAO>> deliverOrder(int userId, int orderId) {
        deliveryService.deliverOrder(userId, orderId);
        return orderLineService.markAsDelivred(orderId);

    }

    @Override
    public List<DeliveryDTO> displayOrders() {
        List<DeliveryDTO> deliveries = new ArrayList<>();
        List<OrderDAO> orders = orderEntityRepository.findAll();
        List<Integer> orderIds = extractOrderIds(orders);
        for (int orderId : orderIds) {
            Optional<DeliveryDTO> delivery = deliveryService.findDelivryByOrderId(orderId);
            if (delivery.isPresent()) {
                deliveries.add(delivery.get());
            }
        }
        return deliveries;
    }

    @Override
    public void scheduleAutoDelivery(int userId, int orderId) {
        deliveryService.scheduleAutoDelivery(userId,orderId);
    }

    private List<Integer> extractOrderIds(List<OrderDAO> orders) {
        List<Integer> orderIds = new ArrayList<>();
        for (OrderDAO order : orders) {
            orderIds.add(order.getId());
        }
        return orderIds;
    }

    private List<ItemDTO> mapItemsJsonNodeToDtoList(JsonNode itemsArray) {
        ObjectMapper mapper = new ObjectMapper();
        List<ItemDTO> itemsDtoList = mapper.convertValue(itemsArray, new TypeReference<List<ItemDTO>>() {});
        return itemsDtoList;
    }

}
