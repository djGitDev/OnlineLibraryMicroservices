package com.onlineLibrary.orchestre.Util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConvertJsonUtils {

    private static final ObjectMapper jacksonMapper = new ObjectMapper();

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

    // Jackson ArrayNode → Gson JsonArray
    public static JsonArray arrayNodeToJsonArray(ArrayNode arrayNode) {
        if (arrayNode == null) {
            return new JsonArray();
        }
        return JsonParser.parseString(arrayNode.toString()).getAsJsonArray();
    }

    // Gson JsonArray → Jackson ArrayNode
    public static ArrayNode jsonArrayToArrayNode(JsonArray gsonArray) throws Exception {
        if (gsonArray == null) {
            return jacksonMapper.createArrayNode();
        }
        return (ArrayNode) jacksonMapper.readTree(gsonArray.toString());
    }
}