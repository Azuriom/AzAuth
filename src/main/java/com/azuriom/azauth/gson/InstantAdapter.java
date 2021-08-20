package com.azuriom.azauth.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

@ApiStatus.Internal
public class InstantAdapter extends TypeAdapter<Instant> {

    @Override
    public void write(JsonWriter out, Instant value) throws IOException {
        out.value(DateTimeFormatter.ISO_INSTANT.format(value));
    }

    @Override
    public Instant read(JsonReader in) throws IOException {
        return DateTimeFormatter.ISO_DATE_TIME.parse(in.nextString(), Instant::from);
    }
}
