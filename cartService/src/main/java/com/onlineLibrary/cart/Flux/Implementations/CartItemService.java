package com.onlineLibrary.cart.Flux.Implementations;

import com.google.gson.JsonObject;
import com.onlineLibrary.cart.Entities.CartItem;
import com.onlineLibrary.cart.Flux.Interfaces.ICartItemsService;
import com.onlineLibrary.cart.Persistance.Interfaces.ICartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService implements ICartItemsService {

    private ICartItemRepository cartItemRepository;

    @Autowired
    public CartItemService(ICartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }


    public Optional<CartItem> cartItemExists(int cartId, int bookId) throws Exception {
        return cartItemRepository.findByCartIdAndBookId(cartId,bookId);
    }


    @Override
    public void updateCartItemQuantity(int cartId, int bookId, int quantity) {
        cartItemRepository.update(cartId,bookId,quantity);
    }

    @Override
    public void insertCartItem(CartItem item) throws Exception {
        cartItemRepository.save(item);
    }

    @Override
    public void clearCartItems(int cartId) {
        try {
            cartItemRepository.deleteByCartId(cartId);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression des items du panier avec l'ID : " + cartId, e);
        }
    }

    @Override
    public void deleteCartItem(int id, int bookId) throws Exception {
        cartItemRepository.deleteItem(id,bookId);
    }

    @Override
    public JsonObject getTotalPrice(int cartId) throws Exception {
        List<CartItem> items = this.getItems(cartId);
        double total = 0.0;
        for (CartItem item : items) {
            total += item.getBookPrice() * item.getQuantity();
        }
        JsonObject result = new JsonObject();
        result.addProperty("status", "success");
        result.addProperty("cart_id", cartId);
        result.addProperty("total_price", total);
        return result;
    }


    @Override
    public List<CartItem> getItems(int cartId) throws Exception {
        return cartItemRepository.findByCartId(cartId);
    }
}
