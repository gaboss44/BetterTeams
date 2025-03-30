package com.booksaw.betterTeams.game.presets;

import com.booksaw.betterTeams.game.scheduler.GameRunnable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.set.ListOrderedSet;

import com.booksaw.betterTeams.Team;

/**
 * This class is used to represent a game, it is used to allow the game to be
 * controlled by the plugin.
 */
public abstract class Game {

    /**
     * The runnable which is used to control the game
     */
    private GameRunnable runnable;

    /**
     * Map of teams which are participating in the game
     */
    private final ListOrderedSet<UUID> participatingTeams = new ListOrderedSet<>();

    /**
     * Starts the game. Does nothing if the game is already running.
     */
    public Game start(boolean callPostStart) {
        if (runnable == null) {
            runnable = new GameRunnable(this,
                    getMaxDuration(),
                    getTickRate(),
                    getStartDelay(),
                    callPostStart);
        }

        return this;
    }

    /**
     * for unscheduling games which are starting (not running yet)
     * 
     * @return
     */
    public Game unschedule() {
        if (runnable != null && runnable.isScheduled()) {
            runnable.unschedule();
            runnable = null;
        }
        return this;
    }

    /**
     * Called when the game starts. To be overriden by subclasses.
     * 
     * @return true if the game should start, false if it should not.
     */
    public boolean onStart() {
        return true;
    }

    public void onPostStart() {
    }

    public Game stop(boolean callPostEnd, boolean forceStop) {
        if (runnable != null && !runnable.hasStopped() && (forceStop || onEnd())) {
            runnable.stop(callPostEnd);
        }
        return this;
    }

    /**
     * Called when the game ends. To be overriden by subclasses.
     * 
     * @return true if the game should end, false if it should last for another game
     *         tick.
     */
    public boolean onEnd() {
        return true;
    }

    public void onPostEnd() {
    }

    public Game togglePause(boolean call) {
        switch (getGameState()) {
            case RUNNING:
                runnable.pause(call);
                break;
            case PAUSED:
                runnable.resume(call);
                break;
            default:
                break;
        }
        return this;
    }

    public void onPause() {
    }

    public void onResume() {
    }

    /**
     * Executes a tick of the game. Used to update the game state.
     */
    public void onTick() {
    }

    public void onPausedTick() {
    }

    /**
     * Adds a team to the list of participating teams.
     *
     * @param team The team to add.
     */
    public void addParticipatingTeam(Team team) {
        participatingTeams.add(team.getID());
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
        return participatingTeams.contains(team.getID());
    }

    /**
     * Gets the set of participating teams.
     *
     * @return A map of participating teams mapped by their UUIDs.
     */
    public ListOrderedSet<UUID> getParticipatingTeams() {
        return participatingTeams;
    }

    public List<Team> resolveTeams() {
        return participatingTeams.stream()
                .map(uuid -> Team.getTeam(uuid))
                .filter(team -> team != null)
                .collect(Collectors.toList());
    }

    /**
     * Gets the maximum duration of the game in ticks.
     *
     * @return The maximum duration of the game.
     */
    public abstract long getMaxDuration();

    public abstract long getStartDelay();

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

    public boolean isScheduled() {
        return runnable != null;
    }

    public boolean isPaused() {
        return runnable != null && runnable.isPaused();
    }

    public boolean isRunning() {
        return runnable != null && runnable.isRunning();
    }

    public boolean isRunningPaused() {
        return runnable != null && runnable.isRunningPaused();
    }

    public boolean isRunningNotPaused() {
        return runnable != null && runnable.isRunningNotPaused();
    }

    public boolean hasStarted() {
        return runnable != null && runnable.hasStarted();
    }

    public boolean hasStopped() {
        return runnable != null && runnable.hasStopped();
    }

    public GameState getGameState() {
        if (runnable == null) {
            return GameState.UNINITIALISED;
        } else if (runnable.hasStarted()) {
            if (runnable.hasStopped()) {
                return GameState.STOPPED;
            } else if (runnable.isPaused()) {
                return GameState.PAUSED;
            } else {
                return GameState.RUNNING;
            }
        } else {
            return GameState.STARTING;
        }
    }

    public enum GameState {
        STARTING, RUNNING, PAUSED, STOPPED, UNINITIALISED
    }
}