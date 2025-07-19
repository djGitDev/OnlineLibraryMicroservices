package com.example.Entities;

public class CartItem {
    private int id;          // Optionnel, clé primaire en base
    private int cartId;      // FK vers le panier
    private int bookId;      // Livre concerné
    private int quantity;     // Quantité du livre dans le panier
    private double bookPrice;

    public CartItem() {}

    public CartItem(int cartId, int bookId, int quantity, double bookPrice) {
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
