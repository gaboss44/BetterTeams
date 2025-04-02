package com.booksaw.betterTeams.game;

import com.booksaw.betterTeams.Main;
import com.booksaw.betterTeams.game.Game;
import com.booksaw.betterTeams.game.storage.GameStorage;
import com.booksaw.betterTeams.game.storage.GameStorageType;

import lombok.Getter;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GameManager {

    @Getter
    private final String GAMES_FOLDER_PATH = "games" + File.separator;
    private final File gamesFolder;
    private final Map<String, GameStorage> gameStorageMap = new HashMap<>();
    private final Map<String, Game> loadedGames = new HashMap<>();

    /**
     * Constructor for initializing the GameManager.
     */
    public GameManager() {
        this.gamesFolder = new File(Main.plugin.getDataFolder() + File.separator + GAMES_FOLDER_PATH);
        if (!gamesFolder.exists() && !gamesFolder.mkdirs()) {
            throw new IllegalStateException("Failed to create the games folder at: " + gamesFolder.getAbsolutePath());
        }
        loadAllGameConfigurations();
    }

    /**
     * Loads all game configurations from the games folder.
     */
    private void loadAllGameConfigurations() {
        for (File file : gamesFolder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".yml")) {
                String gameId = file.getName().replace(".yml", "");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                gameStorageMap.put(gameId, new GameStorage(gameId, file, config));
            }
        }
    }

    /**
     * Loads a game from its configuration.
     *
     * @param gameId The ID of the game to load.
     * @return The loaded game instance.
     */
    public Game loadGame(String gameId) {
        if (loadedGames.containsKey(gameId)) {
            return loadedGames.get(gameId);
        }

        GameStorage storage = gameStorageMap.get(gameId);
        if (storage == null) {
            throw new IllegalArgumentException("No configuration found for the game with ID: " + gameId);
        }

        // Create a game instance using the configuration
        Game game = createGameFromStorage(storage);
        loadedGames.put(gameId, game);
        return game;
    }

    /**
     * Saves the configuration of a specific game to its YAML file.
     *
     * @param gameId The ID of the game to save.
     */
    public void saveGameConfiguration(String gameId) {
        GameStorage storage = gameStorageMap.get(gameId);
        if (storage != null) {
            storage.save();
        }
    }

    /**
     * Saves all loaded game configurations to their respective YAML files.
     */
    public void saveAllGameConfigurations() {
        for (GameStorage storage : gameStorageMap.values()) {
            storage.save();
        }
    }

    /**
     * Creates a new game and its configuration.
     *
     * @param gameId The ID of the new game.
     * @param game   The game instance.
     */
    public void createNewGame(String gameId, Game game) {
        if (gameStorageMap.containsKey(gameId)) {
            throw new IllegalArgumentException("A game with the ID '" + gameId + "' already exists.");
        }

        File gameFile = new File(gamesFolder, gameId + ".yml");
        YamlConfiguration config = new YamlConfiguration();
        GameStorage storage = new GameStorage(gameId, gameFile, config);

        // Save the initial configuration of the game
        storage.setValue("settings", "maxDuration", game.getMaxDuration(), GameStorageType.INTEGER);
        storage.setValue("settings", "startDelay", game.getStartDelay(), GameStorageType.INTEGER);
        storage.setValue("settings", "tickRate", game.getTickRate(), GameStorageType.INTEGER);
        storage.save();

        gameStorageMap.put(gameId, storage);
        loadedGames.put(gameId, game);
    }

    /**
     * Deletes a game and its configuration.
     *
     * @param gameId The ID of the game to delete.
     * @return true if the file was successfully deleted, false otherwise.
     */
    public boolean deleteGame(String gameId) {
        GameStorage storage = gameStorageMap.remove(gameId);
        if (storage != null) {
            loadedGames.remove(gameId);
            return storage.delete();
        }

        File gameFile = new File(gamesFolder, gameId + ".yml");
        return gameFile.exists() && gameFile.delete();
    }

    /**
     * Gets a loaded game by its ID.
     *
     * @param gameId The ID of the game.
     * @return The game instance, or null if it is not loaded.
     */
    public Game getLoadedGame(String gameId) {
        return loadedGames.get(gameId);
    }

    /**
     * Gets all loaded game configurations.
     *
     * @return A map of game IDs to their configurations.
     */
    public Map<String, GameStorage> getAllGameConfigurations() {
        return new HashMap<>(gameStorageMap);
    }

    /**
     * Creates a game instance from its configuration.
     *
     * @param storage The game configuration.
     * @return The game instance.
     */
    private Game createGameFromStorage(GameStorage storage) {
        // Validate and retrieve values from the storage
        long maxDuration = (long) storage.getValue("settings", "maxDuration", GameStorageType.INTEGER);
        long startDelay = (long) storage.getValue("settings", "startDelay", GameStorageType.INTEGER);
        long tickRate = (long) storage.getValue("settings", "tickRate", GameStorageType.INTEGER);

        // Example: Create a generic game
        return new GenericGame(storage.getGameId(), maxDuration, startDelay, tickRate);
    }
}