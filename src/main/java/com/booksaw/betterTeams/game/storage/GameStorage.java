package com.booksaw.betterTeams.game.storage;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class GameStorage {

    private final String gameId;
    private final File file;
    private final YamlConfiguration config;

    public GameStorage(String gameId, File file, YamlConfiguration config) {
        this.gameId = gameId;
        this.file = file;
        this.config = config;
    }

    public String getGameId() {
        return gameId;
    }

    public ConfigurationSection getSection(String sectionName) {
        return config.getConfigurationSection(sectionName);
    }

    public ConfigurationSection createSection(String sectionName) {
        return config.createSection(sectionName);
    }

    public Object getValue(String sectionName, String key, GameStorageType type) {
        ConfigurationSection section = getSection(sectionName);
        if (section == null) {
            return null;
        }
    
        Object value = section.get(key);
        if (!type.isValidType(value)) {
            throw new IllegalArgumentException("Stored value does not match the expected type: " + type);
        }
    
        return value;
    }

    public void setValue(String sectionName, String key, Object value, GameStorageType type) {
        if (!type.isValidType(value)) {
            throw new IllegalArgumentException("Value does not match the expected type: " + type);
        }
    
        ConfigurationSection section = getSection(sectionName);
        if (section == null) {
            section = createSection(sectionName);
        }
        section.set(key, value);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean delete() {
        return file.delete();
    }
}