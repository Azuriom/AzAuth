package com.azuriom.azauth.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class User {

    private String username;
    private UUID uuid;
    private String accessToken;

    private int id;
    private String email;
    private boolean emailVerified;
    private double money;
    private Role role;
    private boolean banned;
    private Instant createdAt;

    /**
     * Internal constructor, should not be use.
     *
     * <p>This prevent Gson to do hacky reflection to instantiate this class.</p>
     */
    private User() {
        // Gson
    }

    /**
     * Create a new PlayerProfile.
     *
     * @param username the player username
     * @param uuid the player unique id
     * @param accessToken the player access token
     * @param email the player email address
     * @param emailVerified is the email address verified
     * @param money the player money
     * @param role the player role
     * @param banned is the player banned
     * @param createdAt the player registration date
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
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the player unique id.
     *
     * @return the player unique id
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Gets the player access token.
     *
     * @return the player access token
     */
    public String getAccessToken() {
        return this.accessToken;
    }

    /**
     * Gets the player email.
     *
     * @return the player email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Verify if the player email address is verified.
     *
     * @return {@code true} if the email address is verified, {@code false} otherwise
     */
    public boolean isEmailVerified() {
        return emailVerified;
    }

    /**
     * Get the player money.
     *
     * @return the player money
     */
    public double getMoney() {
        return money;
    }

    /**
     * Get the player role.
     *
     * @return the player role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Verify if the player is banned.
     *
     * @return {@code true} if the player is banned, {@code false} otherwise
     */
    public boolean isBanned() {
        return banned;
    }

    /**
     * Get the player registration date.
     *
     * @return the player registration date
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User that = (User) o;
        return emailVerified == that.emailVerified &&
                Double.compare(that.money, money) == 0 &&
                banned == that.banned &&
                username.equals(that.username) &&
                uuid.equals(that.uuid) &&
                Objects.equals(accessToken, that.accessToken) &&
                Objects.equals(email, that.email) &&
                Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, uuid, accessToken, email, emailVerified, money, role, banned);
    }

    @Override
    public String toString() {
        return "AuthResult{username='" + username + "', uuid='" + uuid + "'}";
    }
}
