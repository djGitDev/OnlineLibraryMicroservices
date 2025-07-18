package com.example.Persistance.Impl;

import com.example.Persistance.IAuthorBookRepository;
import com.example.Persistance.IDBConnection;
import com.example.UtilInventaire.BeansInjectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuthorBookRepository implements IAuthorBookRepository {

    private IDBConnection dbConnection;


    public AuthorBookRepository(BeansInjectionFactory factory) {
        dbConnection = factory.getDBConnection();
    }

    @Override
    public int createRelation(int idBook, int idAuthor) {
        String sql = "INSERT INTO books_authors (book_id, author_id) VALUES (?, ?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idBook);
            statement.setInt(2, idAuthor);

            return statement.executeUpdate(); // retourne 1 si la relation est insérée

        } catch (SQLException e) {
            // Gestion des doublons : si la relation existe déjà, on ignore (viol. de clé primaire)
            if (e.getSQLState().equals("23505")) { // PostgreSQL: violation de contrainte d'unicité
                return 0; // relation déjà existante
            }
            throw new RuntimeException("Erreur lors de la création de la relation livre-auteur", e);
        }
    }
}
