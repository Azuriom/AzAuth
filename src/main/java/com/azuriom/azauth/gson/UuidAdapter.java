package com.azuriom.azauth.gson;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.util.UUID;

@ApiStatus.Internal
public class UuidAdapter extends TypeAdapter<UUID> {

    @Override
    public void write(JsonWriter out, UUID value) throws IOException {
        out.value(value.toString());
    }

    @Override
    public UUID read(JsonReader in) throws IOException {
        String s = in.nextString();

        try {
            if (s.length() == 32) {
                return fromUndashed(s);
            }

            return UUID.fromString(s);
        } catch (IllegalArgumentException e) {
            throw new JsonSyntaxException("Failed parsing '" + s + "' as UUID", e);
        }
    }

    private UUID fromUndashed(String uuid) {
        return new UUID(
                Long.parseUnsignedLong(uuid.substring(0, 16), 16),
                Long.parseUnsignedLong(uuid.substring(16), 16)
        );
    }
}
