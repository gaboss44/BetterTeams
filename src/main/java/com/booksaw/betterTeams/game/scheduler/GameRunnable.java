package com.booksaw.betterTeams.game.scheduler;

import com.booksaw.betterTeams.Main;
import com.booksaw.betterTeams.game.presets.Game;

import lombok.Getter;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Range;

public class GameRunnable extends BukkitRunnable {

    private final Game game;

    private BukkitTask delayTask;

    @Getter
    private long maxDuration; // Max ticks for the game to run

    @Getter
    private long currentDuration = 0; // Current ticks the game has been running

    @Getter
    private final long startDelay; // Delay before the game starts

    @Getter
    private final long tickRate; // Ticks to wait between each execution

    protected boolean started = false;
    protected boolean callPostStart = false;
    protected boolean paused = false;
    protected boolean stopped = false;

    /**
     * Creates a new GameRunnable.
     *
     * @param game        The game to run.
     * @param maxDuration The maximum duration of the game in ticks.
     * @param tickRate    The rate at which the game should be ticked in ticks.
     */
    public GameRunnable(Game game,
            @Range(from = 0L, to = Long.MAX_VALUE) long maxDuration) {
        this(game, maxDuration, 20L, 0L, false);
    }

    public GameRunnable(Game game,
            @Range(from = 0L, to = Long.MAX_VALUE) long maxDuration,
            boolean callPostStart) {
        this(game, maxDuration, 0L, 0L, callPostStart);
    }

    public GameRunnable(Game game,
            @Range(from = 0L, to = Long.MAX_VALUE) long maxDuration,
            @Range(from = 0L, to = Long.MAX_VALUE) long tickRate) {
        this(game, maxDuration, tickRate, 0L, false);
    }

    public GameRunnable(Game game,
            @Range(from = 0L, to = Long.MAX_VALUE) long maxDuration,
            @Range(from = 0L, to = Long.MAX_VALUE) long tickRate,
            boolean callPostStart) {
        this(game, maxDuration, tickRate, 0L, callPostStart);
    }

    public GameRunnable(Game game,
            @Range(from = 0L, to = Long.MAX_VALUE) long maxDuration,
            @Range(from = 0L, to = Long.MAX_VALUE) long tickRate,
            @Range(from = 0L, to = Long.MAX_VALUE) long startDelay) {
        this(game, maxDuration, tickRate, startDelay, false);
    }

    public GameRunnable(Game game,
            @Range(from = 0L, to = Long.MAX_VALUE) long maxDuration,
            @Range(from = 0L, to = Long.MAX_VALUE) long tickRate,
            @Range(from = 0L, to = Long.MAX_VALUE) long startDelay,
            boolean callPostStart) {
        if (maxDuration < 0L) {
            throw new IllegalArgumentException("Max duration cannot be negative");
        }
        this.maxDuration = maxDuration;
        if (startDelay < 0L) {
            throw new IllegalArgumentException("Start delay cannot be negative");
        }
        this.startDelay = startDelay;
        if (tickRate < 0L) {
            throw new IllegalArgumentException("Tick rate cannot be negative");
        }
        this.tickRate = tickRate;
        this.callPostStart = callPostStart;
        this.game = game;
        schedule();
    }

    @Override
    public void run() {
        if (stopped) {
            return;
        }

        if (paused) {
            game.onPausedTick();
            return;
        }

        game.onTick();

        // if the game state changes in game tick, we need to check if the game is still
        // running
        if (!stopped && !paused) {
            currentDuration++;
            if (getTimeRemaining() <= 0) {
                stop(true);
            }
        }
    }

    private void schedule() {
        if (startDelay > 0L) {
            delayTask = new BukkitRunnable() {
                @Override
                public void run() {
                    start();
                }
            }.runTaskLater(Main.plugin, startDelay);
            return;
        }
        start();
    }

    /**
     * Starts the game.
     */
    private void start() {
        delayTask = null;
        if (!game.onStart()) {
            return;
        }
        started = true;
        if (callPostStart) {
            game.onPostStart();
        }
        runTaskTimer(Main.plugin, 0L, tickRate);
    }

    public boolean hasStarted() {
        return started;
    }

    public void unschedule() {
        delayTask.cancel();
        delayTask = null;
        
    }

    public boolean isScheduled() {
        return delayTask != null;
    }
    
    public void stop(boolean callPostEnd) {
        stopped = true;
        paused = false;
        cancel();
        if (callPostEnd) {
            game.onPostEnd();
        }
    }

    public boolean hasStopped() {
        return stopped;
    }

    /**
     * Pauses the game.
     * 
     * @return true if the game was running and is now paused, false if the game was
     *         already paused or not running.
     */
    public void pause(boolean callPause) {
        paused = true;
        if (callPause) {
            game.onPause();
        }
    }

    public void resume(boolean callResume) {
        paused = false;
        if (callResume) {
            game.onResume();
        }
    }

    public boolean isPaused() {
        return paused;
    }

    /**
     * Gets the time remaining before the game ends.
     *
     * @return The time remaining in ticks.
     */
    public long getTimeRemaining() {
        return maxDuration - currentDuration;
    }

    /**
     * A game is running when the runnable has started and is not stopped.
     */
    public boolean isRunning() {
        return started && !stopped;
    }

    public boolean isRunningPaused() {
        return isRunning() && paused;
    }

    public boolean isRunningNotPaused() {
        return isRunning() && !paused;
    }
}