package com.onlineLibrary.cart.Entities.DAO;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItemDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "cart_id", nullable = false)
    private int cartId;

    @Column(name = "book_id", nullable = false)
    private int bookId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "book_price", nullable = false)
    private double bookPrice;

    public CartItemDAO() {}

    public CartItemDAO(int cartId, int bookId, int quantity, double bookPrice) {
        this.cartId = cartId;
        this.bookId = bookId;
        this.quantity = quantity;
        this.bookPrice = bookPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
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

    public double getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
    }
}