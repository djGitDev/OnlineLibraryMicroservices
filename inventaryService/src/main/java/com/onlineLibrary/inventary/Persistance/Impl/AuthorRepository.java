package com.onlineLibrary.inventary.Persistance.Impl;

import com.onlineLibrary.inventary.Persistance.IAuthorRepository;
import com.onlineLibrary.inventary.Persistance.IDBConnection;
import com.onlineLibrary.inventary.UtilInventaire.BeansInjectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthorRepository implements IAuthorRepository {

    private IDBConnection dbConnection;

    public AuthorRepository(BeansInjectionFactory factory) {
        this.dbConnection = factory.getDBConnection();
    }

    @Override
    public int findAuthorByNameElseCreate(String authorName) {
        String selectSql = "SELECT id FROM authors WHERE name = ?";
        String insertSql = "INSERT INTO authors (name) VALUES (?)";

        try (Connection connection = dbConnection.getConnection()) {

            // Étape 1 : recherche
            try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
                selectStmt.setString(1, authorName);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                }
            }

            // Étape 2 : insertion
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, authorName);
                insertStmt.executeUpdate();

                try (ResultSet rs = insertStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    } else {
                        throw new RuntimeException("Échec de la récupération de l'ID de l'auteur inséré.");
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche ou insertion de l'auteur", e);
        }
    }
}
