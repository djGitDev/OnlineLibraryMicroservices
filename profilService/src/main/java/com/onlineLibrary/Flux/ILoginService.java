package com.onlineLibrary.Flux;

import com.google.gson.JsonObject;

public interface ILoginService {
    JsonObject authentifyUser(JsonObject data) throws Exception;
}
