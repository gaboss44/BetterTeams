package com.booksaw.betterTeams.customEvents;

import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import com.booksaw.betterTeams.Team;

import lombok.Getter;

/**
 * An event which is called right before the restyling of a {@link Team}
 */
@Getter
public class TeamStyleChangeEvent extends TeamEvent {
    private ChatColor newTeamStyle;

    public TeamStyleChangeEvent(
        @NotNull Team team,
        @NotNull ChatColor newTeamStyle) {
        super(team, true);
        this.newTeamStyle = newTeamStyle;
    }

    public void setNewTeamStyle(@NotNull ChatColor newTeamStyle) {
        if (newTeamStyle.isColor()) {
            throw new IllegalArgumentException("Team style cannot be a color.");
        }
        this.newTeamStyle = newTeamStyle;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
