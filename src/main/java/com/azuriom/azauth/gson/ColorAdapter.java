package com.azuriom.azauth.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.ApiStatus;

import java.awt.*;
import java.io.IOException;

@ApiStatus.Internal
public class ColorAdapter extends TypeAdapter<Color> {

    @Override
    public void write(JsonWriter out, Color value) throws IOException {
        out.value('#' + Integer.toHexString(value.getRGB()).substring(2));
    }

    @Override
    public Color read(JsonReader in) throws IOException {
        return Color.decode(in.nextString());
    }
}
