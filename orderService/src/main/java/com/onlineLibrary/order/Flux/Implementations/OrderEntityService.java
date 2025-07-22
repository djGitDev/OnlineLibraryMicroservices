package com.onlineLibrary.order.Flux.Implementations;


import com.google.gson.JsonElement;
import com.onlineLibrary.order.Entities.*;
import com.onlineLibrary.order.Flux.Interfaces.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onlineLibrary.order.Persistance.Interfaces.IOrderEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderEntityService implements IOrderEntityService {



    private InventaryMicroservicesClient microserviceClient;
    private CartMicroservicesClient cartMicroservicesClient;

    private IOrderLineService orderLineService;
    private IDeliveryService deliveryService;
    private IOrderEntityRepository orderEntityRepository;

    @Autowired
    public OrderEntityService
            (InventaryMicroservicesClient microserviceClient,
             CartMicroservicesClient cartMicroservicesClient,
             IOrderLineService orderLineService,
             IDeliveryService deliveryService,
             IOrderEntityRepository orderEntityRepository
            ) {
        this.microserviceClient = microserviceClient;
        this.cartMicroservicesClient = cartMicroservicesClient;
        this.orderLineService = orderLineService;
        this.deliveryService = deliveryService;
        this.orderEntityRepository = orderEntityRepository;
    }


    @Override
    public JsonObject createOrder(int userId, JsonObject jsonUserProfil) throws Exception {
        ResponseEntity<JsonObject>  cart = cartMicroservicesClient.callGetCart(userId);
        JsonObject responseCart = cart.getBody();
        ResponseEntity<JsonObject>  cartItems = cartMicroservicesClient.callGetItems(responseCart.get("cartId").getAsInt());
        JsonObject responseCartItems = cartItems.getBody();
        JsonArray itemsArray = responseCartItems.getAsJsonArray("items");
        for (JsonElement element : itemsArray) {
            JsonObject itemObj = element.getAsJsonObject();

            int bookId = itemObj.get("bookId").getAsInt();
            int quantity = itemObj.get("quantity").getAsInt();
            microserviceClient.callDecreaseBookQuantity(bookId,quantity);
        }

        // Création de la commande
        Order order = new Order(userId);
        int orderId = orderEntityRepository.save(order);
        List<OrderLine> orderLines = orderLineService.convertCartItemsToOrderLines(itemsArray, orderId);
        order.setLignes(orderLines);

        // Planification de la livraison
        Delivery delivery = deliveryService.scheduleDelivery(order.getId(), jsonUserProfil);

        // Nettoyage du panier
        ResponseEntity<JsonObject> responseCartClearedInfo = cartMicroservicesClient.callClearCart(userId);
        JsonObject cartClearedInfo = responseCartClearedInfo.getBody();


        // Construction de la réponse JSON
        JsonObject response = new JsonObject();
        response.addProperty("orderId", orderId);
        response.addProperty("deliveryId", delivery.getId());
        response.add("cartClearedInfo", cartClearedInfo); // Ajoute tout l'objet JSON de confirmation

        return response;
    }

    @Override
    public JsonObject deliverOrder(int orderId) {
        deliveryService.deliverOrder(orderId);
        return orderLineService.markAsDelivred(orderId);
    }

    @Override
    public JsonObject displayOrders() {
        JsonArray jsonOrders = new JsonArray();
        JsonObject orders = orderEntityRepository.fetchAllOrders();
        int[] ordersId = extractOrderIds(orders.getAsJsonArray("orders"));
        for (int orderId : ordersId) {
            JsonObject jsonDelivry = deliveryService.findDelivryByOrderId(orderId);
            jsonOrders.add(jsonDelivry);
        }
        JsonObject response = new JsonObject();
        response.add("orders_with_status", jsonOrders);
        return response;
    }

    @Override
    public void scheduleAutoDelivery(int userId) {
        int orderId = orderEntityRepository.getOrderByUserId(userId);
        deliveryService.scheduleAutoDelivery(orderId);
    }

    int[] extractOrderIds(JsonArray ordersArray) {
        int[] orderIds = new int[ordersArray.size()];

        for (int i = 0; i < ordersArray.size(); i++) {
            JsonObject orderObj = ordersArray.get(i).getAsJsonObject();
            int orderId = orderObj.get("order_id").getAsInt();
            orderIds[i] = orderId;
        }
        return orderIds;
    }

}
