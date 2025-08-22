package com.onlineLibrary.order.Entities.DTO;

import com.fasterxml.jackson.databind.JsonNode;

public class OrderResponseDTO {
    private int orderId;
    private int deliveryId;
    private JsonNode cartClearedInfo;

    // Constructeur, getters et setters
    public OrderResponseDTO() {}

    public OrderResponseDTO(int orderId, int deliveryId, JsonNode cartClearedInfo) {
        this.orderId = orderId;
        this.deliveryId = deliveryId;
        this.cartClearedInfo = cartClearedInfo;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public JsonNode getCartClearedInfo() {
        return cartClearedInfo;
    }

    public void setCartClearedInfo(JsonNode cartClearedInfo) {
        this.cartClearedInfo = cartClearedInfo;
    }
}
