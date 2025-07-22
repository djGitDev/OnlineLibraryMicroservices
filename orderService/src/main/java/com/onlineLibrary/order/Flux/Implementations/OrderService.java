package com.onlineLibrary.order.Flux.Implementations;
import com.onlineLibrary.order.Flux.Interfaces.IOrderEntityService;
import com.onlineLibrary.order.Flux.Interfaces.IOrderService;
import com.onlineLibrary.order.Flux.Interfaces.ProfilMicroservicesClient;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class OrderService implements IOrderService {

    private ProfilMicroservicesClient profilMicroservicesClient;
    private IOrderEntityService orderEntityService;

    @Autowired
    public OrderService
            (ProfilMicroservicesClient microserviceClient,
             IOrderEntityService orderEntityService
            ) {
        this.profilMicroservicesClient = microserviceClient;
        this.orderEntityService = orderEntityService;
    }


    @Override
    public JsonObject placeOrder(int userId,boolean autoDelivery) throws Exception {

        ResponseEntity<JsonObject> respense = profilMicroservicesClient.callGetUserProfil(userId);
        JsonObject jsonUserProfil= respense.getBody().getAsJsonObject();
        JsonObject response = orderEntityService.createOrder(userId,jsonUserProfil);
        if(autoDelivery){
            orderEntityService.scheduleAutoDelivery(userId);
        }
        return response;
    }

    @Override
    public JsonObject deliveryOrder(int orderId) {
        return orderEntityService.deliverOrder(orderId);
    }

    @Override
    public JsonObject displayAllOrders() {
        return orderEntityService.displayOrders();
    }


    @Override
    public JsonObject displayOrders() {
        return orderEntityService.displayOrders();
    }


}
