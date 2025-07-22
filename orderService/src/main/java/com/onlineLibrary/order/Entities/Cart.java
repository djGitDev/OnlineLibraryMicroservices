package com.onlineLibrary.order.Entities;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int id;               // Identifiant unique du panier
    private int UserId;    // Propriétaire du panier
    private List<CartItem> items = new ArrayList<>();


    public Cart(int utilisateurId) {
        this.UserId = utilisateurId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        this.UserId = userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}