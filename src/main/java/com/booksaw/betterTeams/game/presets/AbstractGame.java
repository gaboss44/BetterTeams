package com.booksaw.betterTeams.game.presets;

import com.booksaw.betterTeams.game.task.GameRunnable;
import com.booksaw.betterTeams.game.GameManager;

/**
 * This class is used to represent a game, it is used to allow the game to be
 * controlled by the plugin.
 */
public abstract class AbstractGame {

    private GameRunnable runnable; // Task to run the game
    private boolean running = false; // If the game is currently running

    /**
     * Starts the game. Does nothing if the game is already running.
     */
    public synchronized void start() {
        if (running) {
            throw new IllegalStateException("The game '" + getId() + "' is already running.");
        }

        // Register the game with GameManager
        if (!GameManager.getInstance().startGame(this)) {
            throw new IllegalStateException("Cannot start the game '" + getId() + "': another game is already running.");
        }

        // Initialize and start the runnable
        runnable = new GameRunnable(this, getMaxDuration(), getTickRate());
        runnable.start();

        running = true;
        onStart();
    }

    /**
     * Called when the game starts. To be implemented by subclasses.
     */
    public abstract void onStart();

    /**
     * Stops the game. Does nothing if the game is already stopped.
     */
    public synchronized void stop() {
        if (!running) {
            throw new IllegalStateException("The game " + getId() + "is not running.");
        }

        // Stop the runnable and unregister the game
        if (runnable != null) {
            runnable.cancel();
        }
        GameManager.getInstance().stopCurrentGame();

        running = false;
        onEnd();
    }

    /**
     * Called when the game ends. To be implemented by subclasses.
     */
    public abstract void onEnd();

    /**
     * Pauses the game. Does nothing if the game is not running or already paused.
     */
    public synchronized void pause() {
        if (!running || runnable == null || runnable.isCancelled()) {
            throw new IllegalStateException("The team game " + getId() + "is not running or already paused.");
        }

        runnable.cancel();
        running = false;
    }

    /**
     * Resumes the game. Does nothing if the game is already running or not paused.
     */
    public synchronized void resume() {
        if (running) {
            throw new IllegalStateException("The game '" + getId() + "' is already running.");
        }

        if (runnable == null) {
            throw new IllegalStateException("The game '" + getId() + "' cannot be resumed: no runnable is associated.");
        }

        runnable.start();
        running = true;
    }

    /**
     * Executes a tick of the game. Used to update the game state.
     */
    public abstract void onTick();

    /**
     * Gets the maximum duration of the game in ticks.
     *
     * @return The maximum duration of the game.
     */
    public abstract int getMaxDuration();

    /**
     * Gets the tick rate of the game (ticks between each execution).
     *
     * @return The tick rate of the game.
     */
    public abstract long getTickRate();

    /**
     * Gets the id of the game with which it was created.
     *
     * @return The id of the game.
     */
    public abstract String getId();

    /**
     * Checks if the game is currently running.
     *
     * @return true if the game is running, false otherwise.
     */
    public boolean isRunning() {
        return running;
    }
}