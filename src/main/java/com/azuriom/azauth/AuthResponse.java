package com.azuriom.azauth;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AuthResponse {

    private final String status;
    private final String message;

    public AuthResponse(@NotNull String status, @NotNull String message) {
        this.status = Objects.requireNonNull(status, "status");
        this.message = Objects.requireNonNull(message, "message");
    }

    /**
     * Get the response status.
     *
     * @return the response status
     */
    public @NotNull String getStatus() {
        return this.status;
    }

    /**
     * Get the response message.
     *
     * @return the response message
     */
    public @NotNull String getMessage() {
        return this.message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuthResponse that = (AuthResponse) o;
        return this.status.equals(that.status) && this.message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.status, this.message);
    }

    @Override
    public String toString() {
        return "AuthStatus{status='" + this.status + "', message='" + this.message + "'}";
    }
}
