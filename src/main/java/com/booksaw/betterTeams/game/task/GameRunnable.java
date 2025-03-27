package com.booksaw.betterTeams.game.task;

import com.booksaw.betterTeams.Main;
import com.booksaw.betterTeams.game.presets.AbstractGame;

import lombok.Getter;

import org.bukkit.scheduler.BukkitRunnable;

public class GameRunnable extends BukkitRunnable {

    private final AbstractGame game;

    @Getter
    private final int maxDuration; // Max ticks for the game to run

    @Getter
    private int currentDuration = 0; // Current ticks the game has been running

    @Getter
    private final long tickRate; // Ticks to wait between each execution

    public GameRunnable(AbstractGame game, int maxDuration, long tickRate) {
        this.game = game;
        this.maxDuration = maxDuration;
        this.tickRate = tickRate;
    }

    @Override
    public void run() {
        // Increment the current duration
        currentDuration++;

        // Execute a tick of the game
        if (!isCancelled()) {
            game.onTick();
        }

        // If the game has been running for the max duration, stop the game
        if (currentDuration >= maxDuration) {
            game.stop();
            cancel();
        }
    }

    /**
     * Starts the runnable.
     */
    public void start() {
        runTaskTimer(Main.plugin, 0L, tickRate);
    }

    /**
     * Gets the time remaining before the game ends.
     *
     * @return The time remaining in ticks.
     */
    public int getTimeRemaining() {
        return maxDuration - currentDuration;
    }
}