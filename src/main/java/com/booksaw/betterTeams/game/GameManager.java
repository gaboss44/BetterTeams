package com.booksaw.betterTeams.game;

import com.booksaw.betterTeams.game.presets.AbstractGame;

public class GameManager {

    private static GameManager instance; // Singleton instance
    private AbstractGame currentGame; // Current loaded game

    private GameManager() {
    }

    /**
     * Returns the singleton instance of GameManager.
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Starts a new game if no other game is currently running.
     *
     * @param game The game to start.
     * @return true if the game was successfully started, false if another game is already running.
     */
    public synchronized boolean startGame(AbstractGame game) {
        if (currentGame != null && currentGame.isRunning()) {
            return false; // A game is already running
        }

        currentGame = game;
        return true;
    }

    /**
     * Stops the currently running game.
     */
    public synchronized void stopCurrentGame() {
        if (currentGame != null && currentGame.isRunning()) {
            currentGame.stop();
        }
        currentGame = null;
    }

    /**
     * Gets the currently loaded game.
     *
     * @return The currently loaded game, or null if no game is loaded.
     */
    public AbstractGame getCurrentGame() {
        return currentGame;
    }

    /**
     * Checks if a game is currently running.
     *
     * @return true if a game is running, false otherwise.
     */
    public boolean isGameRunning() {
        return currentGame != null && currentGame.isRunning();
    }
}