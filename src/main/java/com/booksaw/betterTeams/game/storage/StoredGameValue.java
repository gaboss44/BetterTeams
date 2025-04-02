package com.booksaw.betterTeams.game.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing stored game values and their associated types.
 */
@Getter
@RequiredArgsConstructor
public enum StoredGameValue {

    /**
     * The maximum duration of the game (in ticks).
     */
    MAX_DURATION("maxDuration", GameStorageType.INTEGER),

    /**
     * The start delay of the game (in ticks).
     */
    START_DELAY("startDelay", GameStorageType.INTEGER),

    /**
     * The tick rate of the game.
     */
    TICK_RATE("tickRate", GameStorageType.INTEGER),

    /**
     * The winning score for score-based games.
     */
    WINNING_SCORE("winningScore", GameStorageType.INTEGER),

    /**
     * The region ID for region-based games.
     */
    REGION("region", GameStorageType.STRING),

    /**
     * A list of region IDs associated with the game.
     */
    REGION_LIST("regions", GameStorageType.LIST);

    private final String key; // The key used in the configuration file
    private final GameStorageType type; // The type of value associated with the key

    /**
     * Validates if a given value matches the expected type for this stored value.
     *
     * @param value The value to validate.
     * @return true if the value matches the expected type, false otherwise.
     */
    public boolean isValidType(Object value) {
        return type.isValidType(value);
    }
}