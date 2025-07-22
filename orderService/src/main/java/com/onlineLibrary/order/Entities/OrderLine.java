package com.onlineLibrary.order.Entities;


import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderLine {

    private int orderLineId;
    private int orderId;
    private int bookId;
    private int quantity;
    private String statutLivraison = "En attente de livraison";
    private LocalDate dateLivraisonEffective;

    public OrderLine() {
    }

    public OrderLine(int orderId, int bookId, int quantity) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public int getLivreId() {
        return bookId;
    }

    public int getQuantite() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatutLivraison() {
        return statutLivraison;
    }

    public void setStatutLivraison(String statutLivraison) {
        this.statutLivraison = statutLivraison;
    }

    public LocalDate getDateLivraisonEffective() {
        return dateLivraisonEffective;
    }

    public void setDateLivraisonEffective(LocalDateTime localDateTime) {
        localDateTime = localDateTime;
    }

    public void setId(int orderLineId) {
        this.orderLineId = orderLineId;
    }

}

