package com.booksaw.betterTeams.game;

import com.booksaw.betterTeams.game.scheduler.GameRunnable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.set.ListOrderedSet;
import org.bukkit.OfflinePlayer;

import com.booksaw.betterTeams.Team;

/**
 * This class is used to represent a game, it is used to allow the game to be
 * controlled by the plugin.
 */
public class Game {

    private static GameManager gameManager;

    public static void setupGameManager() {
        if (gameManager != null) {
            throw new IllegalStateException("GameManager is already set up.");
        } else {
            gameManager = new GameManager();
        }
    }

    /**
     * The runnable which is used to control the game
     */
    private GameRunnable runnable;

    private final String id;

    /**
     * Map of teams which are participating in the game
     */
    private final ListOrderedSet<UUID> participatingTeams = new ListOrderedSet<>();

    private final ListOrderedSet<OfflinePlayer> participatingPlayers = new ListOrderedSet<>();

    /**
     * Creates a new game with the given id
     *
     * @param id The id of the game
     */
    public Game(String id) {
        this.id = id;
    }

    private void setupRunnable() {
        runnable = new GameRunnable(this,
                getMaxDuration(),
                getTickRate(),
                getStartDelay(),
                false);
    }
    public GameResponse schedule(boolean callPostStart) {
        GameState state = getGameState();
        if (state.isInitialised()) {
            return GameResponse.ALREADY;
        }
        if (runnable == null) {
            if (!onSchedule()) {
                return GameResponse.CANCELLED;
            }
            
            runnable = new GameRunnable(this,
                    getMaxDuration(),
                    getTickRate(),
                    getStartDelay(),
                    callPostStart);
            return GameResponse.SUCCESS;
        }

        return GameResponse.ALREADY;
    }

    public boolean onSchedule() {
        return true;
    }

    public GameResponse unschedule() {
        if (this.isScheduled()) {
            runnable.unschedule();
            runnable = null;
            return GameResponse.SUCCESS;
        }
        return GameResponse.FAILURE;
    }

    private void cleanup() {
        runnable = null;
        participatingPlayers.clear();
        participatingTeams.clear();
        customCleanup();
    }

    public void customCleanup() {
        // Custom cleanup logic can be added here by subclasses
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

    public GameResponse stop(boolean forceStop, boolean callPostEnd) {
        GameState state = getGameState();

        if (state.isRunning()) {
            if (forceStop || onEnd()) {
                runnable.stop(callPostEnd);
                cleanup();
                return GameResponse.SUCCESS;
            } else {
                return GameResponse.CANCELLED;
            }
        } else if (state.isStopped()) {
            return GameResponse.ALREADY;
        } else if (state.isScheduled()) {
            return unschedule();
        } else {
            return GameResponse.FAILURE;
        }
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

    public GameResponse togglePause(boolean call) {
        switch (getGameState()) {
            case TICKING:
                runnable.pause(call);
                return GameResponse.SUCCESS;
            case PAUSED:
                runnable.resume(call);
                return GameResponse.SUCCESS;
            default:
                break;
        }
        return GameResponse.FAILURE;
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

    public void addParticipatingPlayer(OfflinePlayer player) {
        participatingPlayers.add(player);
    }

    /**
     * Removes a team from the list of participating teams.
     *
     * @param team The team to remove.
     */
    public void removeParticipatingTeam(Team team) {
        participatingTeams.remove(team.getID());
    }

    public void removeParticipatingPlayer(OfflinePlayer player) {
        participatingPlayers.remove(player);
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

    public boolean isPlayerParticipating(OfflinePlayer player) {
        return participatingPlayers.contains(player);
    }

    /**
     * Gets the set of participating teams.
     *
     * @return A map of participating teams mapped by their UUIDs.
     */
    public ListOrderedSet<UUID> getParticipatingTeams() {
        return participatingTeams;
    }

    public List<OfflinePlayer> getParticipatingPlayers() {
        return participatingPlayers.stream()
                .filter(player -> player != null)
                .collect(Collectors.toList());
    }

    public List<Team> resolveParticipatingTeams() {
        return participatingTeams.stream()
                .map(uuid -> Team.getTeam(uuid))
                .filter(team -> team != null)
                .collect(Collectors.toList());
    }

    public abstract long getMaxDuration();

    public abstract long getStartDelay();

    public abstract long getTickRate();

    public abstract String getId();

    public boolean isPaused() {
        return getGameState().isPaused();
    }

    public boolean isRunning() {
        return getGameState().isRunning();
    }

    public boolean isTicking() {
        return getGameState().isTicking();
    }

    public boolean isStopped() {
        return getGameState().isStopped();
    }

    public boolean isScheduled() {
        return getGameState().isScheduled();
    }

    public boolean isUninitialised() {
        return getGameState().isUninitialised();
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
                return GameState.TICKING;
            }
        } else if (runnable.isScheduled()) {
            return GameState.SCHEDULED;
        } else {
            return GameState.INITIALISED;
        }
    }

    public enum GameState {
        SCHEDULED, TICKING, PAUSED, STOPPED, UNINITIALISED, INITIALISED;

        public boolean isScheduled() {
            return this == SCHEDULED;
        }

        public boolean isRunning() {
            return this == TICKING || this == PAUSED;
        }

        public boolean isPaused() {
            return this == PAUSED;
        }

        public boolean isStopped() {
            return this == STOPPED;
        }

        public boolean isUninitialised() {
            return this == UNINITIALISED;
        }

        public boolean isTicking() {
            return this == TICKING;
        }

        public boolean isInitialised() {
            return this == INITIALISED;
        }
    }

    public enum GameResponse {
        SUCCESS, FAILURE, ALREADY, CANCELLED;
    }
}