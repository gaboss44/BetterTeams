package com.booksaw.betterTeams.game.storage;

import java.util.List;

/**
 * Enum representing the types of values that can be stored in game configurations.
 */
public enum GameStorageType {

    STRING(String.class),
    INTEGER(Integer.class),
    DOUBLE(Double.class),
    BOOLEAN(Boolean.class),
    LIST(List.class),
    SECTION(Object.class); // Represents a nested configuration section

    private final Class<?> valueType;

    /**
     * Constructor for GameStorageType.
     *
     * @param valueType The class type of the value.
     */
    GameStorageType(Class<?> valueType) {
        this.valueType = valueType;
    }

    /**
     * Gets the class type of the value.
     *
     * @return The class type of the value.
     */
    public Class<?> getValueType() {
        return valueType;
    }

    /**
     * Validates if the given value matches the expected type for this storage type.
     *
     * @param value The value to validate.
     * @return true if the value matches the expected type, false otherwise.
     */
    public boolean isValidType(Object value) {
        if (value == null) {
            return false;
        }
        return valueType.isInstance(value);
    }
}