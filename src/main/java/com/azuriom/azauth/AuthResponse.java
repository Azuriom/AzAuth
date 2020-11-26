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
        return this.status;
    }

    public String getMessage() {
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
