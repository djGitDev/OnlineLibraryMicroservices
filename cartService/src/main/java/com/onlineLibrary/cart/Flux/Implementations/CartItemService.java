package com.onlineLibrary.cart.Flux.Implementations;

import com.onlineLibrary.cart.Entities.DAO.CartItemDAO;
import com.onlineLibrary.cart.Entities.DTO.CartTotalPriceDTO;
import com.onlineLibrary.cart.Flux.Interfaces.ICartItemsService;
import com.onlineLibrary.cart.Persistance.ICartItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService implements ICartItemsService {
    private static final Logger logger = LoggerFactory.getLogger(CartItemService.class);

    private ICartItemRepository cartItemRepository;

    @Autowired
    public CartItemService(ICartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }


    public Optional<CartItemDAO> cartItemExists(int cartId, int bookId) throws Exception {
        return cartItemRepository.findByCartIdAndBookId(cartId,bookId);
    }

    @Override
    public void updateCartItemQuantity(int cartId, int bookId, int quantity) {
        cartItemRepository.update(cartId,bookId,quantity);
    }

    @Override
    public void insertCartItem(CartItemDAO item) throws Exception {
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
        cartItemRepository.deleteByCartIdAndBookId(id,bookId);
    }

    @Override
    public CartTotalPriceDTO getTotalPrice(int cartId) throws Exception {
        List<CartItemDAO> items = this.getItems(cartId);
        double total = items.stream()
                .mapToDouble(item -> item.getBookPrice() * item.getQuantity())
                .sum();
        return new CartTotalPriceDTO( cartId, total,"success");
    }

    @Override
    public void deleteByBookId(int bookId) {
        cartItemRepository.deleteByBookId(bookId);
    }


    @Override
    public List<CartItemDAO> getItems(int cartId) throws Exception {
        return cartItemRepository.findByCartId(cartId);
    }
}
