package com.azuriom.azauth.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

@ApiStatus.Internal
public class InstantSerializer implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return DateTimeFormatter.ISO_DATE_TIME.parse(json.getAsString(), Instant::from);
    }

    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(DateTimeFormatter.ISO_INSTANT.format(src));
    }
}
