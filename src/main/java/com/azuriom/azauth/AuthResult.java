package com.azuriom.azauth;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public interface AuthResult<T> {

    default boolean isSuccess() {
        return this instanceof Success;
    }

    default Success<T> asSuccess() {
        return (Success<T>) this;
    }

    default T getSuccessResult() {
        return asSuccess().getResult();
    }

    default boolean isPending() {
        return this instanceof Pending;
    }

    default Pending<T> asPending() {
        return (Pending<T>) this;
    }

    default Pending.Reason getPendingReason() {
        return asPending().getReason();
    }

    class Success<T> implements AuthResult<T> {
        private final T result;

        public Success(@NotNull T result) {
            this.result = Objects.requireNonNull(result);
        }

        public @NotNull T getResult() {
            return result;
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
            return result.hashCode();
        }

        @Override
        public String toString() {
            return "Success{result=" + result + '}';
        }
    }

    class Pending<T> implements AuthResult<T> {
        private final Reason reason;

        public Pending(Reason reason) {
            this.reason = reason;
        }

        public Reason getReason() {
            return reason;
        }

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
            return "Pending{reason=" + reason + '}';
        }

        public enum Reason {
            REQUIRE_2FA
        }
    }
}
