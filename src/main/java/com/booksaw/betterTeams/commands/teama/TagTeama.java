package com.booksaw.betterTeams.commands.teama;

import com.booksaw.betterTeams.CommandResponse;
import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.commands.presets.TeamSelectSubCommand;
import com.booksaw.betterTeams.util.TeamUtil;

import org.bukkit.command.CommandSender;

import java.util.List;

public class TagTeama extends TeamSelectSubCommand {

	@Override
	public CommandResponse onCommand(CommandSender sender, String label, String[] args, Team team) {
		CommandResponse response = TeamUtil.verifyTagName(args[1], team);
		if (response != null && !args[0].equals(team.getName())) {
			return response;
		}

		team.setTag(args[1]);

		return new CommandResponse(true, "admin.tag.success", team.getMiniDisplayName());
	}

	@Override
	public String getCommand() {
		return "tag";
	}

	@Override
	public String getNode() {
		return "admin.tag";
	}

	@Override
	public String getHelp() {
		return "Change the tag of the specified team";
	}

	@Override
	public String getArguments() {
		return "<team> <tag>";
	}

	@Override
	public int getMinimumArguments() {
		return 2;
	}

	@Override
	public int getMaximumArguments() {
		return 2;
	}

	@Override
	public void onTabComplete(List<String> options, CommandSender sender, String label, String[] args) {
		if (args.length == 1) {
			addTeamStringList(options, args[0]);
		} else if (args.length == 2) {
			options.add("<tag>");
		}
	}

}
