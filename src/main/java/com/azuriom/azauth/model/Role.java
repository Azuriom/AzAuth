package com.azuriom.azauth.model;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

/**
 * Represents a user's role on the website.
 */
public class Role {

    private final String name;
    private final Color color;

    /**
     * Create a new Role.
     *
     * @param name  the role name
     * @param color the role color
     */
    public Role(@NotNull String name, @NotNull Color color) {
        this.name = Objects.requireNonNull(name, "name");
        this.color = Objects.requireNonNull(color, "color");
    }

    /**
     * Gets the role name.
     *
     * @return the role name
     */
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * Gets the role color.
     *
     * @return the role color
     */
    public @NotNull Color getColor() {
        return this.color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        return this.name.equals(role.name) && this.color.equals(role.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.color);
    }

    @Override
    public String toString() {
        return "Role{name='" + this.name + "'}";
    }
}
