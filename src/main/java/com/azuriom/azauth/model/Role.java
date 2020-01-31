package com.azuriom.azauth.model;

import java.awt.*;
import java.util.Objects;

public class Role {

    private String name;
    private Color color;

    /**
     * Internal constructor, should not be use.
     *
     * <p>This prevent Gson to do hacky reflection to instantiate this class.</p>
     */
    private Role() {
        // Gson
    }

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
        return name;
    }

    /**
     * Gets the role color.
     *
     * @return the role color
     */
    public Color getColor() {
        return color;
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
        return Objects.equals(name, role.name) && Objects.equals(color, role.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }

    @Override
    public String toString() {
        return "Role{name='" + name + "'}";
    }
}
