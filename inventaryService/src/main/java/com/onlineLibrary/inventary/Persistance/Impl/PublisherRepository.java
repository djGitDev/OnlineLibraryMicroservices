package com.onlineLibrary.inventary.Persistance.Impl;

import com.onlineLibrary.inventary.Persistance.IDBConnection;
import com.onlineLibrary.inventary.Persistance.IPublisherRepository;
import com.onlineLibrary.inventary.UtilInventaire.BeansInjectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PublisherRepository implements IPublisherRepository {
    private IDBConnection dbConnection;

    public PublisherRepository(BeansInjectionFactory factory) {
        this.dbConnection = factory.getDBConnection();
    }

    @Override
    public int findPublisherByNameElseCreate(String publisherName) {
        String selectSql = "SELECT id FROM publishers WHERE name = ?";
        String insertSql = "INSERT INTO publishers (name) VALUES (?)";

        try (Connection connection = dbConnection.getConnection()) {

            // Étape 1 : recherche
            try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
                selectStmt.setString(1, publisherName);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id"); // trouvé
                    }
                }
            }

            // Étape 2 : insertion
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, publisherName);
                insertStmt.executeUpdate();

                try (ResultSet rs = insertStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // nouvel ID généré
                    } else {
                        throw new RuntimeException("Échec de la récupération de l'ID de l'éditeur inséré.");
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche ou insertion de l'éditeur", e);
        }
    }
}
