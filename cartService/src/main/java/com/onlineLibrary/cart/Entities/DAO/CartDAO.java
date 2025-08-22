package com.onlineLibrary.cart.Entities.DAO;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class CartDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @OneToMany
    @JoinColumn(name = "cart_id")
    private List<CartItemDAO> items = new ArrayList<>();

    public CartDAO() {
    }

    public CartDAO(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<CartItemDAO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDAO> items) {
        this.items = items;
    }
}
