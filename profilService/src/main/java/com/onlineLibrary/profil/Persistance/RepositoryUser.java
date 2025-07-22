package com.onlineLibrary.profil.Persistance;

import com.google.gson.JsonObject;
import com.onlineLibrary.profil.Entities.User;
import com.onlineLibrary.profil.UtilProfil.IBeansInjectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class RepositoryUser implements IRepositoryUser {

   private IDBConnection dbConnection;

    public RepositoryUser(IBeansInjectionFactory factory) {
        this.dbConnection = factory.getDBConnection();
    }

    @Override
    public int createUser(User user) throws SQLException {
        if (user == null) {
            throw new IllegalArgumentException("L'objet User ne peut pas être null");
        }

        String sql = "INSERT INTO users (first_name, last_name, email, phone, password) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (Connection connexion = dbConnection.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {

            // Set parameters from User object
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getPassword());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("Échec de la création de l'utilisateur, aucun ID généré");
                }
            }
        }
    }

    public IDBConnection getConnector() {
        return  dbConnection;
    }

    @Override
    public Optional<User> findUserByEmail(String email) throws Exception {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("L'email ne peut pas être null ou vide");
        }

        String sql = "SELECT id, first_name, last_name, email, phone, password FROM users WHERE email = ?";

        try (Connection connexion = dbConnection.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPhone(resultSet.getString("phone"));
                    user.setPassword(resultSet.getString("password"));

                    return Optional.of(user);
                } else {
                    return Optional.empty(); // aucun utilisateur trouvé
                }
            }

        } catch (SQLException e) {
            throw new Exception("Erreur lors de la recherche de l'utilisateur par email", e);
        }
    }

    @Override
    public JsonObject findUserDataById(int userId) throws Exception {
        String sql = "SELECT id, first_name, last_name, email, phone FROM users WHERE id = ?";

        try (Connection connexion = dbConnection.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    JsonObject jsonUser = new JsonObject();
                    jsonUser.addProperty("id", resultSet.getInt("id"));
                    jsonUser.addProperty("firstName", resultSet.getString("first_name"));
                    jsonUser.addProperty("lastName", resultSet.getString("last_name"));
                    jsonUser.addProperty("email", resultSet.getString("email"));
                    jsonUser.addProperty("phone", resultSet.getString("phone"));
                    return jsonUser;
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new Exception("Erreur lors de la recherche des datas de l'utilisateur", e);
        }
    }
}


