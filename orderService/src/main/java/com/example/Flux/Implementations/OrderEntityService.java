package com.example.Flux.Implementations;


import com.example.Entities.*;
import com.example.Flux.Interfaces.*;
import com.example.Flux.Interfaces.InventaryMicroservicesClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.example.Persistance.Interfaces.IOrderEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class OrderEntityService implements IOrderEntityService {



    private InventaryMicroservicesClient microserviceClient;
    private ICartService cartService;
    private IOrderLineService orderLineService;
    private IDeliveryService deliveryService;
    private IOrderEntityRepository orderEntityRepository;

    @Autowired
    public OrderEntityService(InventaryMicroservicesClient microserviceClient, ICartService cartService,IOrderLineService orderLineService,IDeliveryService deliveryService,IOrderEntityRepository orderEntityRepository) {
        this.microserviceClient = microserviceClient;
        this.cartService = cartService;
        this.orderLineService = orderLineService;
        this.deliveryService = deliveryService;
        this.orderEntityRepository = orderEntityRepository;
    }


    @Override
    public JsonObject createOrder(int userId, JsonObject jsonUserProfil) throws Exception {
        Optional<Cart> cartOptional = cartService.getCart(userId);

        if (!cartOptional.isPresent()) {
            throw new Exception("Aucun panier trouvé pour l'utilisateur : " + userId);
        }

        Cart cart = cartOptional.get();
        List<CartItem> cartItems = cartService.getItems(cart.getId());

        for (CartItem item : cartItems) {
            int bookId = item.getBookId();
            int quantity = item.getQuantity();
            microserviceClient.callDecreaseBookQuantity(bookId,quantity);

        }
        if (cartItems.isEmpty()) {
            throw new Exception("Le panier est vide, impossible de créer une commande.");
        }

        // Création de la commande
        Order order = new Order(userId);
        int orderId = orderEntityRepository.save(order);
        List<OrderLine> orderLines = orderLineService.convertCartItemsToOrderLines(cartItems, orderId);
        order.setLignes(orderLines);

        // Planification de la livraison
        Delivery delivery = deliveryService.planifierLivraison(order.getId(), jsonUserProfil);

        // Nettoyage du panier
        JsonObject cartClearedInfo = cartService.clearCart(userId);

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
