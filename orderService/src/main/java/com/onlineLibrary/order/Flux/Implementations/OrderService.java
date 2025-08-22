package com.onlineLibrary.order.Flux.Implementations;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineLibrary.order.Entities.DAO.OrderLineDAO;
import com.onlineLibrary.order.Entities.DTO.AddressDTO;
import com.onlineLibrary.order.Entities.DTO.DeliveryDTO;
import com.onlineLibrary.order.Entities.DTO.OrderResponseDTO;
import com.onlineLibrary.order.Flux.Interfaces.IOrderEntityService;
import com.onlineLibrary.order.Flux.Interfaces.IOrderService;
import com.onlineLibrary.order.Flux.Interfaces.ProfilMicroservicesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
public class OrderService implements IOrderService {

    private final ProfilMicroservicesClient profilMicroservicesClient;
    private final IOrderEntityService orderEntityService;
    private final ObjectMapper objectMapper;

    @Autowired
    public OrderService
            (ProfilMicroservicesClient microserviceClient,
             IOrderEntityService orderEntityService,
             ObjectMapper objectMapper
            ) {
        this.profilMicroservicesClient = microserviceClient;
        this.orderEntityService = orderEntityService;
        this.objectMapper = objectMapper;
    }


    @Override
    public OrderResponseDTO placeOrder(int userId,boolean autoDelivery) throws Exception {

        ResponseEntity<JsonNode> respenseJackson = profilMicroservicesClient.callGetUserProfil(userId);
        AddressDTO addressDTO = objectMapper.treeToValue(respenseJackson.getBody(), AddressDTO.class);
        OrderResponseDTO response = orderEntityService.createOrder(userId,addressDTO);
        int orderId = response.getOrderId();
        if(autoDelivery){
            orderEntityService.scheduleAutoDelivery(userId,orderId);
        }
        return response;
    }

    @Override
    public Optional<List<OrderLineDAO>> deliveryOrder(int userId, int orderId) {
        return orderEntityService.deliverOrder(userId, orderId);
    }

    @Override
    public List<DeliveryDTO> displayAllOrders() {
        return orderEntityService.displayOrders();
    }


}
