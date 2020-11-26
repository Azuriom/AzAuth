package com.azuriom.azauth.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.awt.*;
import java.lang.reflect.Type;

public class ColorSerializer implements JsonSerializer<Color>, JsonDeserializer<Color> {

    @Override
    public Color deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Color.decode(json.getAsString());
    }

    @Override
    public JsonElement serialize(Color src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive('#' + Integer.toHexString(src.getRGB()).substring(2));
    }
}
