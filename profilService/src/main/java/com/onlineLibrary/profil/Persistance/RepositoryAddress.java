package com.onlineLibrary.profil.Persistance;

import com.google.gson.JsonObject;
import com.onlineLibrary.profil.Entities.Address;
import com.onlineLibrary.profil.UtilProfil.IBeansInjectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepositoryAddress implements IRepositoryAddress {

    private final IDBConnection dbConnection;

    public RepositoryAddress(IBeansInjectionFactory factory) {
        this.dbConnection = factory.getDBConnection();
    }

    @Override
    public int createAddressUser(Address address) throws SQLException {
        if (address == null) {
            throw new IllegalArgumentException("L'objet Address ne peut pas être null");
        }

        String sql = "INSERT INTO user_profiles (street, city, postal_code, province, country, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set parameters from Address object
            statement.setString(1, address.getStreet());
            statement.setString(2, address.getCity());
            statement.setString(3, address.getPostalCode());
            statement.setString(4, address.getProvince());
            statement.setString(5, address.getCountry());
            statement.setInt(6, address.getUserId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("Échec de la création de l'adresse, aucun ID généré");
                }
            }
        }
    }

    @Override
    public JsonObject findUserProfilById(int userId) throws Exception {
        String sql = "SELECT street, city, postal_code, province, country " +
                "FROM user_profiles WHERE user_id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToJson(resultSet);
                }
                return null; // ou retourner un JsonObject vide selon vos besoins
            }
        }
    }

    private void setAddressParameters(PreparedStatement statement, Address address) throws SQLException {
        statement.setString(1, address.getStreet());
        statement.setString(2, address.getCity());
        statement.setString(3, address.getPostalCode());
        statement.setString(4, address.getProvince());
        statement.setString(5, address.getCountry());
        statement.setInt(6, address.getUserId());
    }

    private JsonObject mapResultSetToJson(ResultSet resultSet) throws SQLException {
        JsonObject json = new JsonObject();
        json.addProperty("street", resultSet.getString("street"));
        json.addProperty("city", resultSet.getString("city"));
        json.addProperty("postal_code", resultSet.getString("postal_code"));
        json.addProperty("province", resultSet.getString("province"));
        json.addProperty("country", resultSet.getString("country"));
        return json;
    }

}
