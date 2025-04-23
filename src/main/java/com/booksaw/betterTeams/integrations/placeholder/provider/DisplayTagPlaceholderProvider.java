package com.booksaw.betterTeams.integrations.placeholder.provider;

import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.integrations.placeholder.IndividualTeamPlaceholderProvider;
import com.booksaw.betterTeams.message.MessageManager;

public class DisplayTagPlaceholderProvider implements IndividualTeamPlaceholderProvider {

	@Override
	public String getPlaceholderForTeam(Team team) {
		return MessageManager.getMessage("placeholder.displaytag", team.getDisplayTag());
	}
}
