package com.onlineLibrary.profil.Flux;

import com.google.gson.JsonObject;

public interface ILoginService {
    JsonObject authentifyUser(JsonObject data) throws Exception;
}
