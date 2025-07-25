package com.onlineLibrary.inventary.Persistance.Impl;

import com.onlineLibrary.inventary.Persistance.IAuthorBookRepository;
import com.onlineLibrary.inventary.Persistance.IDBConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class AuthorBookRepository implements IAuthorBookRepository {

    private IDBConnection dbConnection;

    @Autowired
    public AuthorBookRepository(IDBConnection dbConnection) {
        dbConnection = dbConnection;
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
