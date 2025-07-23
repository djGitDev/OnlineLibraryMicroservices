package com.onlineLibrary.order.Entities;


import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderLine {

    private int orderLineId;
    private int orderId;
    private int bookId;
    private int quantity;
    private String DeliveryStatut = "PENDING DELIVERY";
    private LocalDate EffectifDeliveryDate;

    public OrderLine() {
    }

    public OrderLine(int orderId, int bookId, int quantity) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public int getBookId() {
        return bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDeliveryStatut() {
        return DeliveryStatut;
    }

    public void setDeliveryStatut(String deliveryStatut) {
        this.DeliveryStatut = deliveryStatut;
    }

    public LocalDate getEffectifDeliveryDate() {
        return EffectifDeliveryDate;
    }

    public void setEffectifDeliveryDate(LocalDateTime localDateTime) {
        localDateTime = localDateTime;
    }

    public void setId(int orderLineId) {
        this.orderLineId = orderLineId;
    }

}

