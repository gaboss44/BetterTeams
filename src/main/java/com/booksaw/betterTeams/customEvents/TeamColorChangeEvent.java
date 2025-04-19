package com.booksaw.betterTeams.customEvents;

import com.booksaw.betterTeams.Team;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * An event which is called right before the recoloring of a {@link Team}
 */
@Getter
public class TeamColorChangeEvent extends TeamEvent {
	@NotNull
	private ChatColor newTeamColor;

	public TeamColorChangeEvent(
			@NotNull Team team,
			@NotNull ChatColor newColor) {
		super(team, true);
		this.newTeamColor = newColor;
	}

	public void setNewTeamColor(@NotNull ChatColor newColor) {
		if (!newColor.isColor()) {
			throw new IllegalArgumentException("Team color must be an actual color.");
		}
		this.newTeamColor = newColor;
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
