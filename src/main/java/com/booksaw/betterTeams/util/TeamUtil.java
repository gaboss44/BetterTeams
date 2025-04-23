package com.booksaw.betterTeams.util;

import com.booksaw.betterTeams.CommandResponse;
import com.booksaw.betterTeams.Main;
import com.booksaw.betterTeams.Team;
import org.jetbrains.annotations.Nullable;

public class TeamUtil {
	private TeamUtil() {

	}

	public static @Nullable CommandResponse verifyTeamName(@Nullable String teamName) {
		return verifyTeamName(teamName, null);
	}

	public static @Nullable CommandResponse verifyTeamName(@Nullable String teamName, @Nullable Team team) {
		if (!Team.isValidTeamName(teamName)) {
			if (team != null)
				return new CommandResponse("create.banned", teamName, team != null ? team.getMiniDisplayName() : null);
		}

		int max = Math.min(55, Main.plugin.getConfig().getInt("maxTeamLength"));
		if (max != -1 && max < teamName.length()) {
			return new CommandResponse("create.maxLength", teamName, team != null ? team.getMiniDisplayName() : null);
		}

		int min = Math.max(0, Math.min(55, Main.plugin.getConfig().getInt("minTeamLength")));
		if (min != 0 && min > teamName.length()) {
			return new CommandResponse("create.minLength", teamName, team != null ? team.getMiniDisplayName() : null);
		}

		return null;
	}

	public static @Nullable CommandResponse verifyTagName(@Nullable String tagName) {
		return verifyTagName(tagName, null);
	}

	public static @Nullable CommandResponse verifyTagName(@Nullable String tagName, @Nullable Team team) {
		if (!Team.isValidTeamName(tagName)) {
			return new CommandResponse("tag.banned", tagName, team != null ? team.getMiniDisplayName() : null);
		}

		int max = Math.min(55, Main.plugin.getConfig().getInt("maxTagLength"));
		if (max != -1 && max < tagName.length()) {
			return new CommandResponse("tag.maxLength", tagName, team != null ? team.getMiniDisplayName() : null);
		}

		return null;
	}
}
