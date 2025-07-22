package com.onlineLibrary.cart.Persistance.Interfaces;

import com.onlineLibrary.cart.Entities.Cart;

import java.util.Optional;

public interface ICartRepository {
    Optional<Cart> findByUserId(int userId) throws Exception;
    void save(Cart cart) throws Exception;
    void deleteCart(int id);
}
