package com.example.Flux.Interfaces;

import com.google.gson.JsonObject;
import com.example.Entities.CartItem;

import java.util.List;
import java.util.Optional;

public interface ICartItemsService {
    Optional<CartItem> cartItemExists(int cartId, int bookId) throws Exception;
    void updateCartItemQuantity(int cartId, int bookId, int quantity);
    List<CartItem> getItems(int cartId) throws Exception;
    void insertCartItem(CartItem cartItem) throws Exception;
    void clearCartItems(int id);
    void deleteCartItem(int id, int bookId) throws Exception;
    JsonObject getTotalPrice(int cartId) throws Exception;
}
