package com.example.Flux.Implementations;

import com.example.Flux.Interfaces.*;
import com.example.Flux.Interfaces.ProfilMicroservicesClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderService implements IOrderService {

    private ProfilMicroservicesClient profilMicroservicesClient;
    private ICartService cartService;
    private IOrderEntityService orderEntityService;

    @Autowired
    public OrderService(ProfilMicroservicesClient microserviceClient, ICartService cartService,IOrderEntityService orderEntityService) {
        this.profilMicroservicesClient = microserviceClient;
        this.cartService = cartService;
        this.orderEntityService = orderEntityService;
    }

    @Override
    public JsonObject clearCart(int userId) throws Exception {
        return cartService.clearCart(userId);
    }

    @Override
    public JsonObject clearBooks(int userId, JsonObject data ) {
        JsonArray books = data.getAsJsonArray("books");
        return cartService.clearBooks(userId, books);
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
    public JsonObject addSearchedItemsToCart(int userId, JsonObject data , Map<Integer,Double> searchedBooksIds) throws Exception {
        JsonArray books = data.getAsJsonArray("books");
        return cartService.addSearchedItems(userId, books,searchedBooksIds);    }

    @Override
    public JsonObject getTotalPriceCart(int cartId) throws Exception {
        return cartService.getTotalPrice(cartId);
    }


}
