package com.onlineLibrary.cart.Util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConvertJsonUtils {

    private static final ObjectMapper jacksonMapper = new ObjectMapper();
    private static final Gson gson = new Gson();

    // Gson → Jackson
    public static JsonNode gsonToJackson(JsonObject gsonObject) throws Exception {
        String jsonString = gsonObject.toString();
        return jacksonMapper.readTree(jsonString);
    }

    // Jackson → Gson
    public static JsonObject jacksonToGson(JsonNode jacksonNode) {
        String jsonString = jacksonNode.toString();
        return JsonParser.parseString(jsonString).getAsJsonObject();
    }
}