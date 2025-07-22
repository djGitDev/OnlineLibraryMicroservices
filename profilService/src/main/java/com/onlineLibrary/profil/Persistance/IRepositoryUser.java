package com.onlineLibrary.profil.Persistance;

import com.google.gson.JsonObject;
import com.onlineLibrary.profil.Entities.User;

import java.util.Optional;

public interface IRepositoryUser {
    int createUser(User user) throws Exception;
    Optional<User> findUserByEmail(String email) throws Exception;
    JsonObject findUserDataById(int userId) throws Exception;
}