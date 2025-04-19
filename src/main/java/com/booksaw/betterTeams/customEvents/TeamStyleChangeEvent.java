package com.booksaw.betterTeams.customEvents;

import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.booksaw.betterTeams.Team;

import lombok.Getter;

/**
 * An event which is called right before the restyling of a {@link Team}
 */
@Getter
public class TeamStyleChangeEvent extends TeamEvent {
    @Nullable
    private ChatColor newTeamStyle;

    public TeamStyleChangeEvent(
            @NotNull Team team,
            @Nullable ChatColor newTeamStyle) {
        super(team, true);
        this.newTeamStyle = newTeamStyle;
    }

    public void setNewTeamStyle(@Nullable ChatColor newTeamStyle) {
        if (newTeamStyle != null && newTeamStyle.isColor()) {
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
