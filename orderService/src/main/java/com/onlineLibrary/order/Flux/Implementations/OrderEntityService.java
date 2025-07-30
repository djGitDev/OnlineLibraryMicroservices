package com.onlineLibrary.order.Flux.Implementations;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonElement;
import com.onlineLibrary.order.ControllerRestOrderApi.OrderRestController;
import com.onlineLibrary.order.Entities.*;
import com.onlineLibrary.order.Flux.Interfaces.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onlineLibrary.order.Persistance.Interfaces.IOrderEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.onlineLibrary.order.Util.ConvertJsonUtils.jacksonToGson;


@Service
public class OrderEntityService implements IOrderEntityService {

    private static final Logger logger = LoggerFactory.getLogger(OrderEntityService.class);


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
        ResponseEntity<JsonNode>  cartJackson = cartMicroservicesClient.callGetCart(userId);
        JsonObject responseCart = jacksonToGson(cartJackson.getBody());
        ResponseEntity<JsonNode>  cartItemsJackson = cartMicroservicesClient.callGetItems(responseCart.get("cartId").getAsInt());
        JsonObject responseCartItems = jacksonToGson(cartItemsJackson.getBody());
        JsonArray itemsArray = responseCartItems.getAsJsonArray("items");

        for (JsonElement element : itemsArray) {
            JsonObject itemObj = element.getAsJsonObject();

            int bookId = itemObj.get("bookId").getAsInt();
            int quantity = itemObj.get("quantity").getAsInt();
            microserviceClient.callDecreaseBookQuantity(bookId,quantity);
        }

        // create order
        Order order = new Order(userId);
        int orderId = orderEntityRepository.save(order);
        List<OrderLine> orderLines = orderLineService.convertCartItemsToOrderLines(itemsArray, orderId);
        order.setLignes(orderLines);

        // plan delivery
        Delivery delivery = deliveryService.scheduleDelivery(order.getId(), jsonUserProfil);

        // clean cart
        ResponseEntity<JsonNode> responseCartClearedInfoJackson = cartMicroservicesClient.callClearCart(userId);
        JsonObject cartClearedInfo = jacksonToGson(responseCartClearedInfoJackson.getBody());


        // build JSON response
        JsonObject response = new JsonObject();
        response.addProperty("orderId", orderId);
        response.addProperty("deliveryId", delivery.getId());
        response.add("cartClearedInfo", cartClearedInfo);

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
