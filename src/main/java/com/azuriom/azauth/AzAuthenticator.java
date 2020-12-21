package com.azuriom.azauth;

import com.azuriom.azauth.gson.ColorSerializer;
import com.azuriom.azauth.gson.InstantSerializer;
import com.azuriom.azauth.model.User;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Objects;

public class AzAuthenticator {

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Color.class, new ColorSerializer())
            .registerTypeAdapter(Instant.class, new InstantSerializer())
            .create();

    private final String url;

    /**
     * Construct a new AzAuthenticator instance.
     *
     * @param url the website url
     */
    public AzAuthenticator(@NotNull String url) {
        this.url = Objects.requireNonNull(url, "url");

        if (url.startsWith("http://")) {
            System.err.println("[AzLink] The url use HTTP, this is not secure, please consider to upgrade to HTTPS !");
        }
    }

    /**
     * Gets the website url.
     *
     * @return the website url
     */
    public @NotNull String getUrl() {
        return this.url;
    }

    /**
     * Try to authenticate the player on the website and get his profile.
     *
     * @param email    the player email
     * @param password the player password
     * @return the player profile
     * @throws AuthenticationException if credentials are not valid
     * @throws IOException             if an IO exception occurs
     */
    public @NotNull User authenticate(@NotNull String email, @NotNull String password)
            throws AuthenticationException, IOException {
        return this.authenticate(email, password, User.class);
    }

    /**
     * Try to authenticate the player on the website and get his profile with a given response type.
     *
     * @param email        the player email
     * @param password     the player password
     * @param responseType the class of the response
     * @param <T>          the type of the response
     * @return the player profile
     * @throws AuthenticationException if credentials are not valid
     * @throws IOException             if an IO exception occurs
     */
    public <T> @NotNull T authenticate(@NotNull String email, @NotNull String password, @NotNull Class<T> responseType)
            throws AuthenticationException, IOException {
        JsonObject body = new JsonObject();
        body.addProperty("email", email);
        body.addProperty("password", password);

        return this.post("authenticate", body, responseType);
    }

    /**
     * Verify an access token and get the associated profile.
     *
     * @param accessToken the player access token
     * @return the player profile
     * @throws AuthenticationException if credentials are not valid
     * @throws IOException             if an IO exception occurs
     */
    public @NotNull User verify(@NotNull String accessToken) throws AuthenticationException, IOException {
        return this.verify(accessToken, User.class);
    }

    /**
     * Verify an access token and get the associated profile with a given response type.
     *
     * @param accessToken  the player access token
     * @param responseType the class of the response
     * @param <T>          the type of the response
     * @return the player profile
     * @throws AuthenticationException if credentials are not valid
     * @throws IOException             if an IO exception occurs
     */
    public <T> @NotNull T verify(@NotNull String accessToken, @NotNull Class<T> responseType)
            throws AuthenticationException, IOException {
        JsonObject body = new JsonObject();
        body.addProperty("access_token", accessToken);

        return this.post("verify", body, responseType);
    }

    /**
     * Invalidate the given access token.
     * To get a new valid access token you need to use {@link #authenticate(String, String)} again.
     *
     * @param accessToken the player access token
     * @throws AuthenticationException if credentials are not valid
     * @throws IOException             if an IO exception occurs
     */
    public void logout(@NotNull String accessToken) throws AuthenticationException, IOException {
        JsonObject body = new JsonObject();
        body.addProperty("access_token", accessToken);

        this.post("logout", body, null);
    }

    @Contract("_, _, null -> null; _, _, !null -> !null")
    private <T> T post(@NotNull String endPoint, @NotNull JsonObject body, @Nullable Class<T> responseType)
            throws AuthenticationException, IOException {
        URL url = new URL(this.url + "/api/auth/" + endPoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.addRequestProperty("User-Agent", "AzAuth authenticator v1");
        connection.addRequestProperty("Content-Type", "application/json; charset=utf-8");

        try (OutputStream out = connection.getOutputStream()) {
            out.write(body.toString().getBytes(StandardCharsets.UTF_8));
        }

        if (connection.getResponseCode() == 422) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                AuthResponse status = GSON.fromJson(reader, AuthResponse.class);

                throw new AuthenticationException(status.getMessage());
            }
        }

        if (responseType == null) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            T response = GSON.fromJson(reader, responseType);

            if (response == null) {
                throw new IllegalStateException("Empty JSON response");
            }

            return response;
        }
    }
}
