package com.onlineLibrary.order.Persistance.Implementations;

import com.google.gson.JsonObject;
import com.onlineLibrary.order.Entities.Delivery;
import com.onlineLibrary.order.Persistance.Interfaces.IDBConnection;
import com.onlineLibrary.order.Persistance.Interfaces.IDeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class DeliveryRepository implements IDeliveryRepository {
    private final IDBConnection dbConnection;

    @Autowired
    public DeliveryRepository(IDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public int save(Delivery delivery) throws SQLException {
        String sql = """
            INSERT INTO deliveries(
                order_id, 
                street, 
                city, 
                postal_code, 
                province, 
                country,
                scheduled_date, 
                actual_date, 
                status
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
            """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set address components
            stmt.setInt(1, delivery.getOrderId());
            stmt.setString(2, delivery.getStreet());
            stmt.setString(3, delivery.getCity());
            stmt.setString(4, delivery.getPostalCode());
            stmt.setString(5, delivery.getProvince());
            stmt.setString(6, delivery.getCountry());

            // Set dates
            stmt.setDate(7, Date.valueOf(delivery.getScheduledDate()));
            if (delivery.getActualDate() != null) {
                stmt.setDate(8, Date.valueOf(delivery.getActualDate()));
            } else {
                stmt.setNull(8, Types.DATE);
            }

            // Set status
            stmt.setString(9, delivery.getStatut());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
                throw new SQLException("Échec de la création de la livraison");
            }
        }
    }

    @Override
    public JsonObject deliverOrder(int orderId) {
        JsonObject response = new JsonObject();

        String sql = """
        UPDATE deliveries
        SET status = 'DELIVERED', actual_date = CURRENT_DATE
        WHERE order_id = ?
        RETURNING id
    """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    response.addProperty("status", "success");
                    response.addProperty("message", "Commande livrée avec succès.");
                    response.addProperty("delivery_id", rs.getInt("id"));
                } else {
                    response.addProperty("status", "error");
                    response.addProperty("message", "Aucune livraison trouvée pour cette commande.");
                }
            }

        } catch (SQLException e) {
            response.addProperty("status", "error");
            response.addProperty("message", "Erreur lors de la mise à jour de la livraison : " + e.getMessage());
        }

        return response;
    }
    @Override
    public JsonObject findDeliveryByOrderId(int orderId) {
        String sql = "SELECT order_id, status FROM deliveries WHERE order_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    JsonObject deliveryJson = new JsonObject();
                    deliveryJson.addProperty("order_id", rs.getInt("order_id"));
                    deliveryJson.addProperty("status", rs.getString("status"));
                    return deliveryJson;
                } else {
                    JsonObject emptyJson = new JsonObject();
                    emptyJson.addProperty("error", "Delivery not found for order_id " + orderId);
                    return emptyJson;
                }
            }
        } catch (Exception e) {
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("error", "Database error: " + e.getMessage());
            return errorJson;
        }
    }

    // Méthode supplémentaire pour marquer une livraison comme complétée
    public void markAsDelivered(int deliveryId) throws SQLException {
        String sql = """
            UPDATE deliveries 
            SET status = 'DELIVERED', actual_date = CURRENT_DATE 
            WHERE id = ?
            """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, deliveryId);
            stmt.executeUpdate();
        }
    }
}