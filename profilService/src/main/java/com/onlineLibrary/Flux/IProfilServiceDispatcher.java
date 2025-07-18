package com.onlineLibrary.Flux;

import com.google.gson.JsonObject;

public interface IProfilServiceDispatcher {
    JsonObject handleRegistration(JsonObject data) throws Exception;
    JsonObject handleLogin(JsonObject data) throws Exception;
    JsonObject getProfile(int userId) throws Exception;
    JsonObject getUserData(int userId) throws Exception;
}
