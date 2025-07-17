package com.onlineLibrary.Flux;

import com.google.gson.JsonObject;

public interface IRegisterService {
    JsonObject registerUser(JsonObject data) throws Exception;
}
