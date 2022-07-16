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
     * Create a new UserProfile.
     *
     * @param id            the user id on the website
     * @param username      the user username
     * @param uuid          the user unique id
     * @param accessToken   the user access token
     * @param email         the user email address
     * @param emailVerified is the email address verified
     * @param money         the user money
     * @param role          the user role
     * @param banned        is the user banned
     * @param createdAt     the user registration date
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
     * Gets the user id.
     *
     * @return the user id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the user username.
     *
     * @return the user username
     */
    public @NotNull String getUsername() {
        return this.username;
    }

    /**
     * Gets the user unique id.
     *
     * @return the user unique id
     */
    public @NotNull UUID getUuid() {
        return this.uuid;
    }

    /**
     * Gets the user access token.
     *
     * @return the user access token
     */
    public @NotNull String getAccessToken() {
        return this.accessToken;
    }

    /**
     * Gets the user email.
     *
     * @return the user email address
     */
    public @NotNull String getEmail() {
        return this.email;
    }

    /**
     * Verify if the user email address is verified.
     *
     * @return {@code true} if the email address is verified, {@code false} otherwise
     */
    public boolean isEmailVerified() {
        return this.emailVerified;
    }

    /**
     * Get the user money.
     *
     * @return the user money
     */
    public double getMoney() {
        return this.money;
    }

    /**
     * Get the user role.
     *
     * @return the user role
     */
    public @NotNull Role getRole() {
        return this.role;
    }

    /**
     * Verify if the user is banned.
     *
     * @return {@code true} if the user is banned, {@code false} otherwise
     */
    public boolean isBanned() {
        return this.banned;
    }

    /**
     * Get the user registration date.
     *
     * @return the user registration date
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
