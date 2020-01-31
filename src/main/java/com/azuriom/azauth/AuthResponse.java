package com.azuriom.azauth;

import java.util.Objects;

public class AuthResponse {

    private final String status;
    private final String message;

    public AuthResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
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
        return status.equals(that.status) && message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message);
    }

    @Override
    public String toString() {
        return "AuthStatus{status='" + status + "', message='" + message + "'}";
    }
}
