package com.azuriom.azauth.model;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class User {

    private final String username;
    private final UUID uuid;
    private final String accessToken;

    private final int id;
    private final String email;
    private final boolean emailVerified;
    private final double money;
    private final Role role;
    private final boolean banned;
    private final Instant createdAt;

    /**
     * Create a new PlayerProfile.
     *
     * @param id            the user id on the website
     * @param username      the player username
     * @param uuid          the player unique id
     * @param accessToken   the player access token
     * @param email         the player email address
     * @param emailVerified is the email address verified
     * @param money         the player money
     * @param role          the player role
     * @param banned        is the player banned
     * @param createdAt     the player registration date
     */
    public User(int id, String username, UUID uuid, String accessToken, String email, boolean emailVerified, double money, Role role, boolean banned, Instant createdAt) {
        this.id = id;
        this.username = Objects.requireNonNull(username, "username");
        this.uuid = Objects.requireNonNull(uuid, "uuid");
        this.accessToken = Objects.requireNonNull(accessToken, "accessToken");

        this.email = Objects.requireNonNull(email, "email");
        this.role = Objects.requireNonNull(role, "role");
        this.emailVerified = emailVerified;
        this.money = money;
        this.banned = banned;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }

    /**
     * Gets the player id.
     *
     * @return the player id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the player username.
     *
     * @return the player username
     */
    public @NotNull String getUsername() {
        return this.username;
    }

    /**
     * Gets the player unique id.
     *
     * @return the player unique id
     */
    public @NotNull UUID getUuid() {
        return this.uuid;
    }

    /**
     * Gets the player access token.
     *
     * @return the player access token
     */
    public @NotNull String getAccessToken() {
        return this.accessToken;
    }

    /**
     * Gets the player email.
     *
     * @return the player email address
     */
    public @NotNull String getEmail() {
        return this.email;
    }

    /**
     * Verify if the player email address is verified.
     *
     * @return {@code true} if the email address is verified, {@code false} otherwise
     */
    public boolean isEmailVerified() {
        return this.emailVerified;
    }

    /**
     * Get the player money.
     *
     * @return the player money
     */
    public double getMoney() {
        return this.money;
    }

    /**
     * Get the player role.
     *
     * @return the player role
     */
    public @NotNull Role getRole() {
        return this.role;
    }

    /**
     * Verify if the player is banned.
     *
     * @return {@code true} if the player is banned, {@code false} otherwise
     */
    public boolean isBanned() {
        return this.banned;
    }

    /**
     * Get the player registration date.
     *
     * @return the player registration date
     */
    public @NotNull Instant getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return this.id == user.id &&
                this.username.equals(user.username) &&
                this.uuid.equals(user.uuid) &&
                Objects.equals(this.accessToken, user.accessToken) &&
                this.email.equals(user.email) &&
                this.role.equals(user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.username, this.uuid, this.accessToken, this.email, this.role);
    }

    @Override
    public String toString() {
        return "User{id=" + this.id + ", username='" + this.username + "', uuid='" + this.uuid + "'}";
    }
}
