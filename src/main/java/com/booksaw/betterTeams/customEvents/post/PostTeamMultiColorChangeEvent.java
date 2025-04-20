package com.booksaw.betterTeams.customEvents.post;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.color.MultiColor;
import com.booksaw.betterTeams.customEvents.TeamEvent;

import lombok.Getter;

/**
 * An event which is called after a {@link Team}'s multi-color has been changed.
 * Contains information about both the old and new team multi-colors.
 * This event cannot be cancelled since it occurs after the multi-color change.
 * <p>
 * To modify or cancel the multi-color change, use {@link TeamMultiColorChangeEvent}.
 */
@Getter
public class PostTeamMultiColorChangeEvent extends TeamEvent {
    private final MultiColor oldTeamMultiColor;
    private final MultiColor newTeamMultiColor;

    public PostTeamMultiColorChangeEvent(
            @NotNull Team team,
            @NotNull MultiColor oldTeamMultiColor,
            @NotNull MultiColor newTeamMultiColor) {
        super(team, true);

        this.oldTeamMultiColor = oldTeamMultiColor;
        this.newTeamMultiColor = newTeamMultiColor;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;

    }
}
