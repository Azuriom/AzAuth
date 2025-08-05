package com.azuriom.azauth;

import com.azuriom.azauth.exception.AuthException;
import com.azuriom.azauth.gson.ColorAdapter;
import com.azuriom.azauth.gson.InstantAdapter;
import com.azuriom.azauth.gson.UuidAdapter;
import com.azuriom.azauth.model.ErrorResponse;
import com.azuriom.azauth.model.User;
import com.google.gson.*;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * The authentication client for Azuriom.
 * All methods perform blocking operations and should not be called on a non-blocking thread.
 */
@NullMarked
public class AuthClient {

    private static final Logger LOGGER = Logger.getLogger(AuthClient.class.getName());

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Color.class, new ColorAdapter())
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .registerTypeAdapter(UUID.class, new UuidAdapter())
            .create();

    private final String url;

    /**
     * Construct a new AzAuthenticator instance, with the given website URL.
     *
     * @param url the website url
     */
    public AuthClient(String url) {
        Objects.requireNonNull(url, "url");

        this.url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;

        if (!url.startsWith("https://")) {
            LOGGER.warning("HTTP links are not secure, use HTTPS instead.");
        }
    }

    /**
     * Gets the website url.
     *
     * @return the website url
     */
    public String getUrl() {
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
    public AuthResult<User> login(String email, String password) throws AuthException {
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
    public AuthResult<User> login(String email,
                                  String password,
                                  @Nullable String code2fa) throws AuthException {
        return this.login(email, password, code2fa, User.class);
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
    public <T> AuthResult<T> login(String email,
                                   String password,
                                   Class<T> responseType) throws AuthException {
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
    public User login(String email,
                      String password,
                      Supplier<String> codeSupplier) throws AuthException {
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
    public <T> T login(String email,
                       String password,
                       Supplier<@Nullable String> codeSupplier,
                       Class<T> responseType) throws AuthException {
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
    public <T> AuthResult<T> login(String email,
                                   String password,
                                   @Nullable String code2fa,
                                   Class<T> responseType) throws AuthException {
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
    public User verify(String accessToken) throws AuthException {
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
    public <T> T verify(String accessToken, Class<T> responseType) throws AuthException {
        JsonObject body = new JsonObject();
        body.addProperty("access_token", accessToken);

        AuthResult<T> result = this.post("verify", body, responseType);

        if (!result.isSuccess()) {
            throw new AuthException("Unexpected verification result: " + result);
        }

        return result.asSuccess().getResult();
    }

    /**
     * Invalidate the given access token.
     * To get a new valid access token, you need to use {@link #login(String, String)} again.
     *
     * @param accessToken the user access token
     * @throws AuthException if an error occurs (IO exception, invalid credentials, etc)
     */
    public void logout(String accessToken) throws AuthException {
        JsonObject body = new JsonObject();
        body.addProperty("access_token", accessToken);

        this.post("logout", body, JsonNull.class);
    }

    private <T> AuthResult<T> post(String endpoint, JsonObject body, Class<T> responseType)
            throws AuthException {
        try {
            URI api = URI.create(this.url + "/api/auth/" + endpoint);
            HttpURLConnection connection = (HttpURLConnection) api.toURL().openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.addRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.addRequestProperty("User-Agent", "AzAuth authenticator v1");

            try (OutputStream out = connection.getOutputStream()) {
                out.write(body.toString().getBytes(StandardCharsets.UTF_8));
            }

            int status = connection.getResponseCode();

            if (status >= 400 && status < 500) {
                return this.handleClientError(connection);
            }

            return handleResponse(connection, responseType);
        } catch (IOException e) {
            throw new AuthException(e);
        }
    }

    private <T> AuthResult<T> handleResponse(HttpURLConnection connection, Class<@Nullable T> type)
            throws AuthException, IOException {
        if (type == JsonNull.class) {
            return new AuthResult.Success<>(type.cast(JsonNull.INSTANCE));
        }

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
            throw new AuthException("Invalid JSON response from API (HTTP " + status + ")", e);
        }
    }
}
