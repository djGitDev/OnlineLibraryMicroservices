package com.onlineLibrary.profil.Flux;

import com.google.gson.JsonObject;

public interface IRegisterService {
    JsonObject registerUser(JsonObject data) throws Exception;
}
