package com.booksaw.betterTeams.customEvents;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.color.MultiColor;

import lombok.Getter;
import lombok.Setter;

/**
 * An event which is called right before the recoloring of a {@link Team} with a
 * {@link MultiColor}
 */
@Getter
@Setter
public class TeamMultiColorChangeEvent extends TeamEvent {
    @NotNull
    private MultiColor newTeamMultiColor;

    public TeamMultiColorChangeEvent(
            @NotNull Team team,
            @NotNull MultiColor newTeamMultiColor) {
        super(team, true);

        this.newTeamMultiColor = newTeamMultiColor;
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
