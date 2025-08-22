package com.onlineLibrary.cart.Flux.Implementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.onlineLibrary.cart.Entities.DAO.CartDAO;
import com.onlineLibrary.cart.Entities.DAO.CartItemDAO;
import com.onlineLibrary.cart.Entities.DTO.*;
import com.onlineLibrary.cart.Flux.Interfaces.InventaryMicroservicesClient;
import com.onlineLibrary.cart.Flux.Interfaces.ICartItemsService;
import com.onlineLibrary.cart.Flux.Interfaces.ICartService;
import com.onlineLibrary.cart.Persistance.ICartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class CartService implements ICartService {
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    private InventaryMicroservicesClient microserviceClient;
    private ICartItemsService cartItemService;
    private ICartRepository cartRepository;


    @Autowired
    public CartService(InventaryMicroservicesClient microserviceClient, ICartItemsService cartItemService,ICartRepository cartRepository) {
        this.microserviceClient = microserviceClient;
        this.cartItemService = cartItemService;
        this.cartRepository = cartRepository;
    }


    @Override
    public ClearCartResponseDTO clearCart(int userId) {
        try {
            Optional<CartDAO> optionalCart = cartRepository.findByUserId(userId);

            if (optionalCart.isPresent()) {
                CartDAO cart = optionalCart.get();

                cartItemService.clearCartItems(cart.getId());
                cartRepository.delete(cart);
                return new ClearCartResponseDTO("success", "Cart and all items deleted successfully.", List.of());
            } else {
                return new ClearCartResponseDTO("error", "No cart found for the user.", List.of());
            }
        } catch (Exception e) {
            return new ClearCartResponseDTO("error", "Failed to clear cart: " + e.getMessage(), List.of());
        }
    }

@Override
public ClearBooksResponseDTO clearBooks(int userId, JsonNode booksNode) {
    List<CartItemDAO> updatedItems;

    try {
        Optional<CartDAO> optionalCart = cartRepository.findByUserId(userId);

        if (optionalCart.isEmpty()) {
            return new ClearBooksResponseDTO("error", "No cart found for the user.");
        }

        CartDAO cart = optionalCart.get();
        processBooksRemoval(cart, booksNode);
        updatedItems = cartItemService.getItems(cart.getId());

        if (updatedItems.isEmpty()) {
            cartRepository.delete(cart);
            ClearBooksResponseDTO responseDTO = new ClearBooksResponseDTO("success", "Books removed from cart.", updatedItems);
            responseDTO.setAlert("Cart removed.");
            return responseDTO;
        }else{
            return new ClearBooksResponseDTO("success", "Books removed from cart.", updatedItems);

        }

    } catch (Exception e) {
        return new ClearBooksResponseDTO("error", "Failed to remove books: " + e.getMessage());

    }
}

    @Override
    public ClearBooksResponseDTO clearItem(int userId, int bookId, int quantity) {
        try {
            Optional<CartDAO> optionalCart = cartRepository.findByUserId(userId);

            if (optionalCart.isEmpty()) {
                return new ClearBooksResponseDTO("error", "No cart found for the user.");
            }

            CartDAO cart = optionalCart.get();
            processItemRemoval(cart, bookId, quantity);

            return new ClearBooksResponseDTO("success", "Item removed from cart.");
        } catch (Exception e) {
            return new ClearBooksResponseDTO("error", "Failed to remove item: " + e.getMessage());
        }
    }

    private void processBooksRemoval(CartDAO cart, JsonNode booksNode) throws Exception {
        for (JsonNode bookNode : booksNode) {
            int bookId = bookNode.get("book_id").asInt();
            int quantityToRemove = bookNode.get("quantity").asInt();

            Optional<CartItemDAO> optionalItem = cartItemService.cartItemExists(cart.getId(), bookId);

            if (optionalItem.isPresent()) {
                CartItemDAO existingItem = optionalItem.get();
                int updatedQuantity = existingItem.getQuantity() - quantityToRemove;

                if (updatedQuantity <= 0) {
                    cartItemService.deleteCartItem(cart.getId(), bookId);
                } else {
                    cartItemService.updateCartItemQuantity(cart.getId(), bookId, -quantityToRemove);
                }
            }
        }
    }

    private void processItemRemoval(CartDAO cart,int bookId,int quantityToRemove) throws Exception {
        Optional<CartItemDAO> optionalItem = cartItemService.cartItemExists(cart.getId(), bookId);

        if (optionalItem.isPresent()) {
            CartItemDAO existingItem = optionalItem.get();
            int updatedQuantity = existingItem.getQuantity() - quantityToRemove;

            if (updatedQuantity <= 0) {
                cartItemService.deleteCartItem(cart.getId(), bookId);
            } else {
                cartItemService.updateCartItemQuantity(cart.getId(), bookId, -quantityToRemove);
            }
        }
    }


    @Override
    public CartDTO getCart(int userId) throws Exception {
        Optional<CartDAO> optionalCart = cartRepository.findByUserId(userId);
        CartDAO cart = optionalCart.get();
        return new CartDTO(cart.getId(), cart.getUserId());
    }

    @Override
    public CartItemsResponseDTO getItems(int id) throws Exception {
        List<CartItemDAO> items =cartItemService.getItems(id);
        return new CartItemsResponseDTO("success", id, items);
    }


    @Override
    public AddBooksResponseDTO addSearchedItemsToCart(int userId, JsonNode booksNode, Map<Integer, Double> searchedBooksIds) throws Exception {

        CartDAO cart =getOrCreateCart(userId);

        if (booksNode.isArray()) {
            processBooksForCart(cart, booksNode, searchedBooksIds);
        }

        List<CartItemDAO> items = cartItemService.getItems(cart.getId());

        return new AddBooksResponseDTO(
                "success",
                "Books added to cart",
                cart.getId(),
                items
        );
    }

    @Override
    public AddBookResponseDTO addSingleItemToCart(int userId, int bookId, int quantity, double price) throws Exception {
        CartDAO cart =getOrCreateCart(userId);
        processSingleBook(cart,bookId,quantity,price);
        return new AddBookResponseDTO(
                "success",
                "Book added to cart",
                cart.getId()
        );
    }



    private void processBooksForCart(CartDAO cart, JsonNode booksNode, Map<Integer, Double> searchedBooksIds) throws Exception {
        for (JsonNode bookNode : booksNode) {
            int bookId = bookNode.get("book_id").asInt();
            int quantity = bookNode.get("quantity").asInt();
            double bookPrice = searchedBooksIds.get(bookId);
            processSingleBook(cart, bookId,quantity,bookPrice);
        }
    }


    private void processSingleBook(CartDAO cart,int bookId, int quantity,double bookPrice) throws Exception {
        ResponseEntity<JsonNode> response = microserviceClient.callFindBookById(bookId);
        JsonNode jsonBook = response.getBody();
        JsonNode bookInInventary = jsonBook.get("book");
        int quantityAvailable = bookInInventary.get("quantity").asInt();

        if (quantityAvailable > 0) {
            Optional<CartItemDAO> existingItem = cartItemService.cartItemExists(cart.getId(), bookId);
            if (existingItem.isPresent()) {
                cartItemService.updateCartItemQuantity(cart.getId(), bookId, Math.min(quantity, quantityAvailable));
            } else {
                cartItemService.insertCartItem(new CartItemDAO(
                        cart.getId(),
                        bookId,
                        Math.min(quantity, quantityAvailable),
                        bookPrice
                ));
            }
        }
    }

    private CartDAO getOrCreateCart(int userId) {
        Optional<CartDAO> optionalCart = cartRepository.findByUserId(userId);
        if (optionalCart.isPresent()) {
            return optionalCart.get();
        } else {
            CartDAO cart = new CartDAO(userId);
            cartRepository.save(cart);
            return cart;
        }
    }

    @Override
    public CartTotalPriceDTO getTotalPrice(int cartId) throws Exception {
        return cartItemService.getTotalPrice(cartId);
    }



}