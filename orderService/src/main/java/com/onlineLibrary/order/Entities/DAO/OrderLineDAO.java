package com.onlineLibrary.order.Entities.DAO;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "order_lines")
public class OrderLineDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_line_id")
    private int orderLineId;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "order_id", nullable = false)
//     private OrderDAO order;

    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Column(name = "book_id", nullable = false)
    private int bookId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "delivery_statut", nullable = false)
    private String deliveryStatut = "PENDING DELIVERY";

    @Column(name = "effectif_delivery_date")
    private LocalDate effectifDeliveryDate;

    public OrderLineDAO() {
    }

    public OrderLineDAO(int orderId, int bookId, int quantity) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    // Getters et Setters

    public int getOrderLineId() {
        return orderLineId;
    }

    public void setOrderLineId(int orderLineId) {
        this.orderLineId = orderLineId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDeliveryStatut() {
        return deliveryStatut;
    }

    public void setDeliveryStatut(String deliveryStatut) {
        this.deliveryStatut = deliveryStatut;
    }

    public LocalDate getEffectifDeliveryDate() {
        return effectifDeliveryDate;
    }

    public void setEffectifDeliveryDate(LocalDate effectifDeliveryDate) {
        this.effectifDeliveryDate = effectifDeliveryDate;
    }
}
