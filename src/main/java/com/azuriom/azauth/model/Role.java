package com.azuriom.azauth.model;

import java.awt.*;
import java.util.Objects;

public class Role {

    private String name;
    private Color color;

    /**
     * Internal constructor, should not be used.
     *
     * <p>This prevent Gson to do hacky reflection to instantiate this class.</p>
     */
    private Role() {
        // For Gson
    }

    /**
     * Create a new Role.
     *
     * @param name  the role name
     * @param color the role color
     */
    public Role(String name, Color color) {
        this.name = Objects.requireNonNull(name, "name");
        this.color = Objects.requireNonNull(color, "color");
    }

    /**
     * Gets the role name.
     *
     * @return the role name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the role color.
     *
     * @return the role color
     */
    public Color getColor() {
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
        return Objects.equals(this.name, role.name) &&
                Objects.equals(this.color, role.color);
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
