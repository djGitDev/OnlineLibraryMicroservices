package com.example.Persistance.Impl;

import com.example.UtilInventaire.IBeansInjectionFactory;
import com.example.Persistance.ICategoryRepository;
import com.example.Persistance.IDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CategoryRepository implements ICategoryRepository {

    private IDBConnection dbConnection;

    public CategoryRepository(IBeansInjectionFactory factory) {
        this.dbConnection = factory.getDBConnection();
    }


    @Override
    public int findCategoryByNameElseCreate(String categoryName) {
        String selectSql = "SELECT id FROM categories WHERE name = ?";
        String insertSql = "INSERT INTO categories (name) VALUES (?)";

        try (Connection connection = dbConnection.getConnection()) {

            // Étape 1 : vérifier si la catégorie existe
            try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
                selectStmt.setString(1, categoryName);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                }
            }

            // Étape 2 : insérer si elle n'existe pas
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, categoryName);
                insertStmt.executeUpdate();

                try (ResultSet rs = insertStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    } else {
                        throw new RuntimeException("Impossible de récupérer l'ID de la catégorie insérée.");
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche ou de l'insertion de la catégorie", e);
        }
    }
}
