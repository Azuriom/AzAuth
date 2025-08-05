package com.azuriom.azauth;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * Represents the result of an authentication attempt.
 *
 * @param <T> the type of the value returned by the result
 */
@NullMarked
public interface AuthResult<T> {

    /**
     * Checks if the authentication result is a success.
     * When this method returns true, the result is guaranteed to be an instance of {@link Success}.
     *
     * @return true if the result is a success, false otherwise
     * @see Success
     * @see #asSuccess()
     */
    default boolean isSuccess() {
        return this instanceof Success;
    }

    /**
     * Converts the authentication result to a success result.
     * This method should only be called if {@link #isSuccess()} returns true.
     *
     * @return this instance, as a {@link Success}
     * @throws ClassCastException if the result is not a success
     */
    default Success<T> asSuccess() {
        return (Success<T>) this;
    }

    /**
     * Gets the result value from a successful authentication.
     * This method should only be called if {@link #isSuccess()} returns true.
     *
     * @return the result value
     * @throws ClassCastException if the result is not a success
     * @see #asSuccess()
     * @see #isSuccess()
     * @see Success#getResult()
     */
    default T getSuccessResult() {
        return asSuccess().getResult();
    }

    /**
     * Checks if the authentication result is pending.
     * When this method returns true, the result is guaranteed to be an instance of {@link Pending}.
     *
     * @return true if the result is pending, false otherwise
     * @see Pending
     * @see #asPending()
     */
    default boolean isPending() {
        return this instanceof Pending;
    }

    /**
     * Converts the authentication result to a pending result.
     * This method should only be called if {@link #isPending()} returns true.
     *
     * @return this instance, as a {@link Pending}
     * @throws ClassCastException if the result is not pending
     */
    default Pending<T> asPending() {
        return (Pending<T>) this;
    }

    /**
     * Gets the reason for the pending state.
     * This method should only be called if {@link #isPending()} returns true.
     *
     * @return the reason for the pending state
     * @throws ClassCastException if the result is not pending
     * @see #asPending()
     * @see #isPending()
     * @see Pending#getReason()
     */
    default Pending.Reason getPendingReason() {
        return asPending().getReason();
    }

    /**
     * Represents a successful authentication result, which contains a result value.
     *
     * @param <T> the type of the result value
     */
    class Success<T> implements AuthResult<T> {
        private final T result;

        /**
         * Constructs a new Success instance with the given result.
         *
         * @param result the result of the successful authentication, must not be null
         */
        public Success(T result) {
            this.result = Objects.requireNonNull(result, "result");
        }

        /**
         * Gets the result of the successful authentication.
         *
         * @return the result value
         */
        public T getResult() {
            return this.result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Success<?> success = (Success<?>) o;
            return this.result.equals(success.result);
        }

        @Override
        public int hashCode() {
            return this.result.hashCode();
        }

        @Override
        public String toString() {
            return "Success{result=" + this.result + '}';
        }
    }

    /**
     * Represents a pending authentication result, which indicates that further action is required.
     *
     * @param <T> the type of the value returned by the result
     */
    class Pending<T> implements AuthResult<T> {
        private final Reason reason;

        /**
         * Constructs a new Pending instance, with the given reason.
         *
         * @param reason the reason for the pending state
         */
        public Pending(Reason reason) {
            this.reason = Objects.requireNonNull(reason, "reason");
        }

        /**
         * Gets the reason explaining why the authentication is pending.
         *
         * @return the reason for the pending state
         */
        public Reason getReason() {
            return this.reason;
        }

        /**
         * Checks if the pending state requires two-factor authentication (2FA).
         * This method returns true if the reason is {@link Reason#REQUIRE_2FA}.
         *
         * @return true if 2FA is required, false otherwise
         * @see Reason#REQUIRE_2FA
         */
        public boolean require2fa() {
            return this.reason == Reason.REQUIRE_2FA;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Pending<?> pending = (Pending<?>) o;
            return this.reason == pending.reason;
        }

        @Override
        public int hashCode() {
            return this.reason.hashCode();
        }

        @Override
        public String toString() {
            return "Pending{reason=" + this.reason + '}';
        }

        /**
         * Represents the reason for the pending state of an authentication result.
         */
        public enum Reason {
            /**
             * Indicates that two-factor authentication (2FA) is required to complete the authentication process.
             * In this case, the login method should be called again, with the 2FA code provided by the user.
             */
            REQUIRE_2FA
        }
    }
}
