package com.onlineLibrary.order.Persistance.Interfaces;

import com.onlineLibrary.order.Entities.Cart;

import java.util.Optional;

public interface ICartRepository {
    Optional<Cart> findByUserId(int userId) throws Exception;
    void save(Cart cart) throws Exception;
    void deleteCart(int id);
}
