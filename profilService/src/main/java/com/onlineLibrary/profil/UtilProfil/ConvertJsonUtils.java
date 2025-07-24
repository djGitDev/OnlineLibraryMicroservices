package com.onlineLibrary.profil.UtilProfil;

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
        if (gsonObject == null) {
            return jacksonMapper.createObjectNode();
        }
        String jsonString = gson.toJson(gsonObject);
        return jacksonMapper.readTree(jsonString);
    }

    // Jackson → Gson
    public static JsonObject jacksonToGson(JsonNode jacksonNode) {
        if (jacksonNode == null) {
            return new JsonObject();
        }
        String jsonString = jacksonNode.toString();
        return JsonParser.parseString(jsonString).getAsJsonObject();
    }

    // Génère un JsonNode d'erreur
    public static JsonNode errorToJackson(Exception e) {
        JsonObject error = new JsonObject();
        error.addProperty("error", e.getMessage());
        try {
            return gsonToJackson(error);
        } catch (Exception ex) {
            return jacksonMapper.createObjectNode().put("error", ex.getMessage());
        }
    }
}