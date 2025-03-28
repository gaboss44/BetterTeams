package com.booksaw.betterTeams.game.presets;

import com.booksaw.betterTeams.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This abstract class represents a game where teams compete by scoring points.
 * It extends AbstractGame and provides functionality for managing team scores.
 */
public abstract class ScoreGame extends AbstractGame {

    private final Map<UUID, Integer> teamScores = new HashMap<>(); // Scores for each team

    /**
     * Adds points to a team's score.
     *
     * @param team  The team to add points to.
     * @param points The number of points to add.
     */
    public void addPoints(Team team, int points) {
        if (!isTeamParticipating(team)) {
            throw new IllegalArgumentException("The team is not participating in the game.");
        }

        UUID teamID = team.getID();
        teamScores.put(teamID, teamScores.getOrDefault(teamID, 0) + points);
        onScoreUpdated(team, teamScores.get(teamID));
    }

    /**
     * Subtracts points from a team's score.
     *
     * @param team  The team to subtract points from.
     * @param points The number of points to subtract.
     */
    public void subtractPoints(Team team, int points) {
        if (!isTeamParticipating(team)) {
            throw new IllegalArgumentException("The team is not participating in the game.");
        }

        UUID teamID = team.getID();
        teamScores.put(teamID, Math.max(0, teamScores.getOrDefault(teamID, 0) - points));
        onScoreUpdated(team, teamScores.get(teamID));
    }

    /**
     * Gets the score of a team.
     *
     * @param team The team to get the score for.
     * @return The score of the team.
     */
    public int getScore(Team team) {
        return teamScores.getOrDefault(team.getID(), 0);
    }

    /**
     * Gets the scores of all teams.
     *
     * @return A map of team UUIDs and their scores.
     */
    public Map<UUID, Integer> getTeamScores() {
        return teamScores;
    }

    /**
     * Called when a team's score is updated.
     *
     * @param team  The team whose score was updated.
     * @param newScore The new score of the team.
     */
    public abstract void onScoreUpdated(Team team, int newScore);

    /**
     * Called when a team wins the game.
     *
     * @param winningTeam The team that won the game.
     */
    public abstract void onTeamWin(Team winningTeam);

    /**
     * Checks if a team has reached the winning score.
     *
     * @param team The team to check.
     * @return true if the team has reached the winning score, false otherwise.
     */
    public boolean hasTeamWon(Team team) {
        return getScore(team) >= getWinningScore();
    }

    /**
     * Gets the score required to win the game.
     *
     * @return The winning score.
     */
    public abstract int getWinningScore();
}