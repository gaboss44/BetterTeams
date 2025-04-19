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
	private ChatColor newTeamColor;

	public TeamColorChangeEvent(@NotNull Team team,
								@NotNull ChatColor newColor) {
		super(team, true);

		this.newTeamColor = newColor;
	}

	public void setNewTeamColor(@NotNull ChatColor newColor) {
		if (newColor.isFormat()) {
			throw new IllegalArgumentException("Team color cannot be a style.");
		}
		if (newColor == ChatColor.RESET) {
			throw new IllegalArgumentException("Team color cannot be reset.");
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
