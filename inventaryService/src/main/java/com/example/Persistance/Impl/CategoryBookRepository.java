package com.example.Persistance.Impl;

import com.example.Persistance.ICategoryBookRepository;
import com.example.Persistance.IDBConnection;
import com.example.UtilInventaire.BeansInjectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CategoryBookRepository implements ICategoryBookRepository {

    private IDBConnection dbConnection;


    public CategoryBookRepository(BeansInjectionFactory factory) {
        dbConnection = factory.getDBConnection();
    }

    @Override
    public int createRelation(int idBook, int idCategory) {
        String sql = "INSERT INTO books_categories (book_id, category_id) VALUES (?, ?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idBook);
            statement.setInt(2, idCategory);

            return statement.executeUpdate(); // retourne 1 si l'insertion réussit

        } catch (SQLException e) {
            // Optionnel : ignorer si la relation existe déjà
            if (e.getSQLState().equals("23505")) { // code pour violation de clé primaire (PostgreSQL)
                return 0;
            }
            throw new RuntimeException("Erreur lors de la création de la relation livre-catégorie", e);
        }
    }
}
