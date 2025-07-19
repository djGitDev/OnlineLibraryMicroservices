package com.example.Flux.Implementations;

import com.example.Flux.Interfaces.InventaryMicroservicesClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.example.Entities.Cart;
import com.example.Entities.CartItem;
import com.example.Flux.Interfaces.ICartItemsService;
import com.example.Flux.Interfaces.ICartService;
import com.example.Persistance.Interfaces.ICartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CartService implements ICartService {

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
    public JsonObject clearCart(int userId) {
        JsonObject response = new JsonObject();

        try {
            Optional<Cart> optionalCart = cartRepository.findByUserId(userId);

            if (optionalCart.isPresent()) {
                Cart cart = optionalCart.get();

                // Supprime les items liés à ce panier
                cartItemService.clearCartItems(cart.getId());

                // Supprime le panier lui-même
                cartRepository.deleteCart(cart.getId());

                response.addProperty("status", "success");
                response.addProperty("message", "Cart and all items deleted successfully.");
                response.add("items", new JsonArray());
            } else {
                response.addProperty("status", "error");
                response.addProperty("message", "No cart found for the user.");
            }
        } catch (Exception e) {
            response.addProperty("status", "error");
            response.addProperty("message", "Failed to clear cart: " + e.getMessage());
        }

        return response;
    }

    @Override
    public JsonObject clearBooks(int userId, JsonArray books) {
        JsonObject response = new JsonObject();
        JsonArray updatedItemsArray = new JsonArray();

        try {
            Optional<Cart> optionalCart = cartRepository.findByUserId(userId);

            if (optionalCart.isEmpty()) {
                response.addProperty("status", "error");
                response.addProperty("message", "No cart found for the user.");
                return response;
            }

            Cart cart = optionalCart.get();

            for (int i = 0; i < books.size(); i++) {
                JsonObject book = books.get(i).getAsJsonObject();
                int bookId = book.get("book_id").getAsInt();
                int quantityToRemove = book.get("quantity").getAsInt();


                Optional<CartItem> optionalItem = cartItemService.cartItemExists(cart.getId(), bookId);

                if (optionalItem.isPresent()) {
                    CartItem existingItem = optionalItem.get();
                    int updatedQuantity = existingItem.getQuantity() - quantityToRemove;

                    if (updatedQuantity <= 0) {
                        // Supprimer complètement l'item
                        cartItemService.deleteCartItem(cart.getId(), bookId);
                    } else {
                        // Mettre à jour la quantité
                        cartItemService.updateCartItemQuantity(cart.getId(), bookId, -quantityToRemove);
                    }
                }
            }

            List<CartItem> remainingItems = cartItemService.getItems(cart.getId());
            for (CartItem item : remainingItems) {
                JsonObject itemJson = new JsonObject();
                itemJson.addProperty("book_id", item.getBookId());
                itemJson.addProperty("quantity", item.getQuantity());
                updatedItemsArray.add(itemJson);
            }

            if (remainingItems.isEmpty()) {
                // Plus aucun item dans le panier -> on le supprime aussi
                cartRepository.deleteCart(cart.getId());
                response.addProperty("Alert", "Cart removed.");
            }

            response.addProperty("status", "success");
            response.addProperty("message", "Books removed from cart.");
            response.add("items", updatedItemsArray);

        } catch (Exception e) {
            response.addProperty("status", "error");
            response.addProperty("message", "Failed to remove books: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Optional<Cart> getCart(int userId) throws Exception {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public List<CartItem> getItems(int id) throws Exception {
        return cartItemService.getItems(id);
    }

    @Override
    public JsonObject addSearchedItems(int userId, JsonArray books, Map<Integer, Double> searchedBooksIds) throws Exception {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        Cart cart;
        if (optionalCart.isPresent()) {
            cart = optionalCart.get();
        } else {
            // Crée un nouveau panier
            cart = new Cart(userId);
            cartRepository.save(cart); // Sauvegarde initiale pour générer l'ID si besoin
        }


        // Traite chaque livre à ajouter
        for (int i = 0; i < books.size(); i++) {

            JsonObject book = books.get(i).getAsJsonObject();
            int bookId = book.get("book_id").getAsInt();
            int quantity = book.get("quantity").getAsInt();
            double bookPrice = searchedBooksIds.get(bookId);
            JsonObject body = new JsonObject();
            body.addProperty("bookId", bookId);
            ResponseEntity<JsonObject> respense = microserviceClient.callFindBookById(body);
            JsonObject jsonBook = respense.getBody().getAsJsonObject();
            JsonObject bookInInventary = jsonBook.getAsJsonObject("book");
            int quantityAvailable = bookInInventary.get("quantity").getAsInt();
            if (quantityAvailable > 0) {
                Optional<CartItem> existingItem = cartItemService.cartItemExists(cart.getId(), bookId);
                if (existingItem.isPresent()) {
                    cartItemService.updateCartItemQuantity(cart.getId(), bookId, quantity <= quantityAvailable ? quantity : quantityAvailable);
                } else {
                    cartItemService.insertCartItem(new CartItem(cart.getId(), bookId, quantity <= quantityAvailable ? quantity : quantityAvailable, bookPrice));
                }
            }
        }

        // Reconstruire la liste complète des items du panier
        JsonArray itemsArray = new JsonArray();
        List<CartItem> items = cartItemService.getItems(cart.getId());
        for (CartItem item : items) {
            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("book_id", item.getBookId());
            itemJson.addProperty("quantity", item.getQuantity());
            itemJson.addProperty("bookPrice", item.getBookPrice());

            itemsArray.add(itemJson);
        }

        // Construction de la réponse JSON
        JsonObject response = new JsonObject();
        response.addProperty("status", "success");
        response.addProperty("message", "Books added to cart");
        response.addProperty("message", "Books added to cart");
        response.addProperty("cartId", cart.getId());
        response.add("items", itemsArray);

        return response;
    }

    @Override
    public JsonObject getTotalPrice(int cartId) throws Exception {
        return cartItemService.getTotalPrice(cartId);
    }
}