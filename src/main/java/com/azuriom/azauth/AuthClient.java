package com.azuriom.azauth;

import com.azuriom.azauth.exception.AuthException;
import com.azuriom.azauth.gson.ColorAdapter;
import com.azuriom.azauth.gson.InstantAdapter;
import com.azuriom.azauth.model.ErrorResponse;
import com.azuriom.azauth.model.User;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.jetbrains.annotations.Blocking;
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
import java.util.function.Supplier;
import java.util.logging.Logger;

public class AuthClient {

    private static final Logger LOGGER = Logger.getLogger(AuthClient.class.getName());

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Color.class, new ColorAdapter())
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .create();

    private final String url;

    /**
     * Construct a new AzAuthenticator instance.
     *
     * @param url the website url
     */
    public AuthClient(@NotNull String url) {
        this.url = Objects.requireNonNull(url, "url");

        if (!url.startsWith("https://")) {
            LOGGER.warning("HTTP links are not secure, use HTTPS instead.");
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
     * Try to authenticate the user on the website and get his profile.
     *
     * @param email    the user email
     * @param password the user password
     * @return the user profile
     * @throws AuthException if an error occurs (IO exception, invalid credentials, etc)
     */
    @Blocking
    public @NotNull AuthResult<User> login(@NotNull String email, @NotNull String password) throws AuthException {
        return this.login(email, password, User.class);
    }

    /**
     * Try to authenticate the user on the website with a 2FA code, and get his profile.
     *
     * @param email    the user email
     * @param password the user password
     * @param code2fa  the 2FA code of the user
     * @return the user profile
     * @throws AuthException if an error occurs (IO exception, invalid credentials, etc)
     */
    @Blocking
    public @NotNull AuthResult<User> login(@NotNull String email,
                                           @NotNull String password,
                                           @Nullable String code2fa) throws AuthException {
        return this.login(email, password, User.class);
    }

    /**
     * Try to authenticate the user on the website and get his profile with a given response type.
     *
     * @param email        the user email
     * @param password     the user password
     * @param responseType the class of the response
     * @param <T>          the type of the response
     * @return the user profile
     * @throws AuthException if an error occurs (IO exception, invalid credentials, etc)
     */
    @Blocking
    public <T> @NotNull AuthResult<T> login(@NotNull String email,
                                            @NotNull String password,
                                            @NotNull Class<T> responseType) throws AuthException {
        return login(email, password, (String) null, responseType);
    }

    /**
     * Try to authenticate the user on the website and get his profile with a given response type.
     * If the user has 2FA enabled, the {@code codeSupplier} will be called.
     *
     * @param email        the user email
     * @param password     the user password
     * @param codeSupplier the supplier called to get the 2FA code
     * @return the user profile
     * @throws AuthException if an error occurs (IO exception, invalid credentials, etc)
     */
    @Blocking
    public @NotNull User login(@NotNull String email,
                               @NotNull String password,
                               @NotNull Supplier<String> codeSupplier) throws AuthException {
        return login(email, password, codeSupplier, User.class);
    }

    /**
     * Try to authenticate the user on the website and get his profile with a given response type.
     * If the user has 2FA enabled, the {@code codeSupplier} will be called.
     *
     * @param email        the user email
     * @param password     the user password
     * @param codeSupplier the supplier called to get the 2FA code
     * @param responseType the class of the response
     * @param <T>          the type of the response
     * @return the user profile
     * @throws AuthException if an error occurs (IO exception, invalid credentials, etc)
     */
    @Blocking
    public <T> @NotNull T login(@NotNull String email,
                                @NotNull String password,
                                @NotNull Supplier<String> codeSupplier,
                                @NotNull Class<T> responseType) throws AuthException {
        AuthResult<T> result = login(email, password, responseType);

        if (result.isSuccess()) {
            return result.getSuccessResult();
        }

        if (!result.isPending() || !result.asPending().require2fa()) {
            throw new AuthException("Unknown login result: " + result);
        }

        String code = codeSupplier.get();

        if (code == null) {
            throw new AuthException("No 2FA code provided.");
        }

        result = login(email, password, code, responseType);

        if (!result.isSuccess()) {
            throw new AuthException("Unknown login result: " + result);
        }

        return result.getSuccessResult();
    }

    /**
     * Try to authenticate the user on the website and get his profile with a 2FA code and a given response type.
     *
     * @param email        the user email
     * @param password     the user password
     * @param code2fa      the 2FA code of the user
     * @param responseType the class of the response
     * @param <T>          the type of the response
     * @return the user profile
     * @throws AuthException if an error occurs (IO exception, invalid credentials, etc)
     */
    @Blocking
    public <T> @NotNull AuthResult<T> login(@NotNull String email,
                                            @NotNull String password,
                                            @Nullable String code2fa,
                                            @NotNull Class<T> responseType) throws AuthException {
        JsonObject body = new JsonObject();
        body.addProperty("email", email);
        body.addProperty("password", password);
        body.addProperty("code", code2fa);

        return this.post("authenticate", body, responseType);
    }

    /**
     * Verify an access token and get the associated profile.
     *
     * @param accessToken the user access token
     * @return the user profile
     * @throws AuthException if an error occurs (IO exception, invalid credentials, etc)
     */
    @Blocking
    public @NotNull AuthResult<User> verify(@NotNull String accessToken) throws AuthException {
        return this.verify(accessToken, User.class);
    }

    /**
     * Verify an access token and get the associated profile with a given response type.
     *
     * @param accessToken  the user access token
     * @param responseType the class of the response
     * @param <T>          the type of the response
     * @return the user profile
     * @throws AuthException if an error occurs (IO exception, invalid credentials, etc)
     */
    @Blocking
    public <T> @NotNull AuthResult<T> verify(@NotNull String accessToken, @NotNull Class<T> responseType)
            throws AuthException {
        JsonObject body = new JsonObject();
        body.addProperty("access_token", accessToken);

        return this.post("verify", body, responseType);
    }

    /**
     * Invalidate the given access token.
     * To get a new valid access token you need to use {@link #login(String, String)} again.
     *
     * @param accessToken the user access token
     * @throws AuthException if an error occurs (IO exception, invalid credentials, etc)
     */
    @Blocking
    public void logout(@NotNull String accessToken) throws AuthException {
        JsonObject body = new JsonObject();
        body.addProperty("access_token", accessToken);

        this.post("logout", body, null);
    }

    @Blocking
    @Contract("_, _, null -> null; _, _, !null -> !null")
    private <T> AuthResult<T> post(@NotNull String endPoint, @NotNull JsonObject body, @Nullable Class<T> responseType)
            throws AuthException {
        try {
            return this.doPost(endPoint, body, responseType);
        } catch (IOException e) {
            throw new AuthException(e);
        }
    }

    @Blocking
    @Contract("_, _, null -> null; _, _, !null -> !null")
    private <T> AuthResult<T> doPost(@NotNull String endPoint, @NotNull JsonObject body, @Nullable Class<T> responseType)
            throws AuthException, IOException {
        try {
            URL apiUrl = new URL(this.url + "/api/auth/" + endPoint);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.addRequestProperty("User-Agent", "AzAuth authenticator v1");
            connection.addRequestProperty("Content-Type", "application/json; charset=utf-8");

            try (OutputStream out = connection.getOutputStream()) {
                out.write(body.toString().getBytes(StandardCharsets.UTF_8));
            }

            int status = connection.getResponseCode();

            if (status >= 400 && status < 500) {
                return this.handleClientError(connection);
            }

            if (responseType == null) {
                return null;
            }

            return handleResponse(connection, responseType);
        } catch (IOException e) {
            throw new AuthException(e);
        }
    }

    private <T> AuthResult<T> handleResponse(HttpURLConnection connection, Class<T> type) throws AuthException, IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            T response = GSON.fromJson(reader, type);

            if (response == null) {
                throw new AuthException("Empty JSON response from API");
            }

            return new AuthResult.Success<>(response);
        }
    }

    private <T> AuthResult<T> handleClientError(HttpURLConnection connection)
            throws AuthException, IOException {
        int status = connection.getResponseCode();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
            ErrorResponse response = GSON.fromJson(reader, ErrorResponse.class);

            if (response.getStatus().equals("pending")
                    && Objects.equals(response.getReason(), "2fa")) {
                return new AuthResult.Pending<>(AuthResult.Pending.Reason.REQUIRE_2FA);
            }

            throw new AuthException(response.getMessage());
        } catch (JsonParseException e) {
            throw new AuthException("Invalid JSON response from API (" + status + ")");
        }
    }
}
