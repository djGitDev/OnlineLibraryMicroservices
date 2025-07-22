package com.onlineLibrary.order.Persistance.Implementations;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onlineLibrary.order.Entities.Order;
import com.onlineLibrary.order.Persistance.Interfaces.IDBConnection;
import com.onlineLibrary.order.Persistance.Interfaces.IOrderEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class OrderEntityRepository implements IOrderEntityRepository {

    private final IDBConnection dbConnection;

    private static final String SQL_INSERT_ORDER = """
        INSERT INTO orders(user_id, order_date)
        VALUES (?, CURRENT_DATE)
        RETURNING id;
        """;


    @Autowired
    public OrderEntityRepository(IDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public int save(Order order) throws Exception {

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_ORDER)) {

            stmt.setInt(1, order.getUserId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int orderId = rs.getInt("id");
                    order.setId(orderId);
                    return orderId;
                }
                throw new SQLException("Échec : aucun ID retourné après insertion de la commande.");
            }
        } catch (SQLException e) {
            throw new Exception("Erreur lors de l'enregistrement de la commande : " + e.getMessage(), e);
        }
    }

    @Override
    public JsonObject fetchAllOrders() {
        JsonObject response = new JsonObject();
        JsonArray ordersArray = new JsonArray();

        String sql = "SELECT id, user_id FROM orders";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                JsonObject orderJson = new JsonObject();
                orderJson.addProperty("order_id", rs.getInt("id"));
                orderJson.addProperty("user_id", rs.getInt("user_id"));
                ordersArray.add(orderJson);
            }

            response.addProperty("status", "success");
            response.add("orders", ordersArray);

        } catch (Exception e) {
            response.addProperty("status", "error");
            response.addProperty("message", "Erreur lors de la récupération des commandes : " + e.getMessage());
        }

        return response;
    }

    @Override
    public int getOrderByUserId(int userId) {
        String sql = """
        SELECT id 
        FROM orders 
        WHERE user_id = ? 
        ORDER BY order_date DESC, id DESC 
        LIMIT 1
        """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Aucune commande trouvée pour l'utilisateur avec ID " + userId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // À remplacer par un vrai logger en prod
            return -1;
        }
    }

}