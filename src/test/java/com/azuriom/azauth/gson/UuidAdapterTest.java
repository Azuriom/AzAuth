package com.azuriom.azauth.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UuidAdapterTest {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(UUID.class, new UuidAdapter())
            .create();

    private static final UUID UUID_1 = UUID.fromString("c237bec1-19ef-4858-a98e-521cf0aad4c0");

    @Test
    void testDeserialize() {
        String uuid1 = "\"c237bec1-19ef-4858-a98e-521cf0aad4c0\"";
        String uuid2 = "\"c237bec119ef4858a98e521cf0aad4c0\"";
        assertEquals(UUID_1, GSON.fromJson(uuid1, UUID.class));
        assertEquals(UUID_1, GSON.fromJson(uuid2, UUID.class));
    }

    @Test
    void testSerialize() {
        String expected = "\"c237bec1-19ef-4858-a98e-521cf0aad4c0\"";
        assertEquals(expected, GSON.toJson(UUID_1));
    }
}
