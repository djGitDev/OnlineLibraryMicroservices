package com.onlineLibrary.Persistance;

import com.google.gson.JsonObject;
import com.onlineLibrary.Entities.User;

import java.util.Optional;

public interface IRepositoryUser {
    int createUser(User user) throws Exception;
    Optional<User> findUserByEmail(String email) throws Exception;
    JsonObject findUserDataById(int userId) throws Exception;
}