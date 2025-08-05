package com.azuriom.azauth.exception;

/**
 * Represents an exception that occurs during authentication.
 */
public class AuthException extends Exception {

    /**
     * Constructs a new AuthException with the specified detail message.
     *
     * @param message the detail message
     */
    public AuthException(String message) {
        super(message);
    }

    /**
     * Constructs a new AuthException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new AuthException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public AuthException(Throwable cause) {
        super(cause);
    }
}
