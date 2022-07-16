package com.azuriom.azauth.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ErrorResponse {

    private final String status;
    private final String message;
    private final String reason;

    public ErrorResponse(@NotNull String status, @NotNull String message, @Nullable String reason) {
        this.status = Objects.requireNonNull(status, "status");
        this.message = Objects.requireNonNull(message, "message");
        this.reason = reason;
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

    /**
     * Get the response error/pending reason, or null.
     *
     * @return the reason
     */
    public @Nullable String getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ErrorResponse that = (ErrorResponse) o;
        return this.status.equals(that.status)
                && this.message.equals(that.message)
                && Objects.equals(this.reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.status, this.message, this.reason);
    }

    @Override
    public String toString() {
        return "AuthStatus{status='" + this.status
                + "', message='" + this.message
                + "', reason='" + this.reason
                + "'}";
    }
}