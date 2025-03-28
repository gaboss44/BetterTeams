package com.booksaw.betterTeams.game.presets;

import com.booksaw.betterTeams.game.task.GameRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.game.GameManager;

/**
 * This class is used to represent a game, it is used to allow the game to be
 * controlled by the plugin.
 */
public abstract class AbstractGame {

    /**
     * The runnable which is used to control the game
     */
    private GameRunnable runnable;

    /**
     * Whether or not the game is currently running
     */
    private boolean running = false;

    /**
     * Map of teams which are participating in the game
     */
    private final Map<UUID, Team> participatingTeams = new HashMap<>();

    /**
     * Starts the game. Does nothing if the game is already running.
     */
    public synchronized void start() {
        if (running) {
            throw new IllegalStateException("The game '" + getId() + "' is already running.");
        }

        // Register the game with GameManager
        if (!GameManager.getInstance().startGame(this)) {
            throw new IllegalStateException(
                    "Cannot start the game '" + getId() + "': another game is already running.");
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

        running = false;
        onEnd();
    }

    /**
     * Adds a team to the list of participating teams.
     *
     * @param team The team to add.
     */
    public void addParticipatingTeam(Team team) {
        participatingTeams.put(team.getID(), team);
    }

    /**
     * Removes a team from the list of participating teams.
     *
     * @param team The team to remove.
     */
    public void removeParticipatingTeam(Team team) {
        participatingTeams.remove(team.getID());
    }

    /**
     * Checks if a team is participating in the game.
     *
     * @param team The team to check.
     * @return true if the team is participating, false otherwise.
     */
    public boolean isTeamParticipating(Team team) {
        return participatingTeams.containsKey(team.getID());
    }

    /**
     * Gets the set of participating teams.
     *
     * @return A map of participating teams mapped by their UUIDs.
     */
    public Map<UUID, Team> getParticipatingTeams() {
        return participatingTeams;
    }

    /**
     * Updates the references of participating teams after a reload.
     * This method uses the UUIDs to fetch the updated team objects from
     * TeamManager.
     */
    public void refreshParticipatingTeams() {
        if (participatingTeams.isEmpty())
            return;

        Map<UUID, Team> updatedTeams = new HashMap<>();
        for (UUID uuid : participatingTeams.keySet()) {
            Team updatedTeam = Team.getTeam(uuid);
            if (updatedTeam != null)
                updatedTeams.put(uuid, updatedTeam);
        }
        participatingTeams.clear();
        participatingTeams.putAll(updatedTeams);
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