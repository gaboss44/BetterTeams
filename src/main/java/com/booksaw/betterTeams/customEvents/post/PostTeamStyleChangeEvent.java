package com.booksaw.betterTeams.customEvents.post;

import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.customEvents.TeamEvent;

import lombok.Getter;

/**
 * An event which is called after a {@link Team}'s style has been changed.
 * <p>
 * Contains information about both the old and new team styles.
 * <p>
 * This event cannot be cancelled since it occurs after the style change.
 * <p>
 * To modify or cancel the style change, use {@link TeamStyleChangeEvent}.
 */
@Getter
public class PostTeamStyleChangeEvent extends TeamEvent {
    private final ChatColor oldTeamStyle;
    private final ChatColor newTeamStyle;

    public PostTeamStyleChangeEvent(
        @NotNull Team team,
        @NotNull ChatColor oldTeamStyle,
        @NotNull ChatColor newTeamStyle) {
        super(team, true);
        this.oldTeamStyle = oldTeamStyle;
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
