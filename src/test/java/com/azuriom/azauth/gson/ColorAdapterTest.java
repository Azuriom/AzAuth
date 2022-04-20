package com.azuriom.azauth.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ColorAdapterTest {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Color.class, new ColorAdapter())
            .create();

    private static final Color COLOR_1 = new Color(235, 64, 52);
    private static final Color COLOR_2 = new Color(242, 179, 53);
    private static final Color COLOR_3 = new Color(82, 219, 13);

    @Test
    void testDeserialize() {
        assertEquals(Color.BLACK, GSON.fromJson("\"#000000\"", Color.class));
        assertEquals(Color.WHITE, GSON.fromJson("\"#ffffff\"", Color.class));
        assertEquals(COLOR_1, GSON.fromJson("\"#eb4034\"", Color.class));
        assertEquals(COLOR_2, GSON.fromJson("\"#f2b335\"", Color.class));
        assertEquals(COLOR_3, GSON.fromJson("\"#52db0d\"", Color.class));
    }

    @Test
    void testSerialize() {
        assertEquals("\"#000000\"", GSON.toJson(Color.BLACK));
        assertEquals("\"#ffffff\"", GSON.toJson(Color.WHITE));
        assertEquals("\"#eb4034\"", GSON.toJson(COLOR_1));
        assertEquals("\"#f2b335\"", GSON.toJson(COLOR_2));
        assertEquals("\"#52db0d\"", GSON.toJson(COLOR_3));
    }
}
