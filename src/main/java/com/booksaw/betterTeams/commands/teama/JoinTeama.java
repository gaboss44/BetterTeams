package com.booksaw.betterTeams.commands.teama;

import com.booksaw.betterTeams.CommandResponse;
import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.commands.SubCommand;
import com.booksaw.betterTeams.message.MessageManager;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class JoinTeama extends SubCommand {

	@Override
	public CommandResponse onCommand(CommandSender sender, String label, String[] args) {

		Player p = Bukkit.getPlayer(args[1]);
		if (p == null) {
			return new CommandResponse("noPlayer");
		}

		Team team = Team.getTeam(args[0]);
		if (team == null) {
			return new CommandResponse(p, "notTeam");
		}

		Team temp = Team.getTeam(p);
		if (temp != null) {
			// replacement is provided in case team display name is wanted
			return new CommandResponse(p, "admin.notInTeam", temp.getMiniDisplayName());
		}

		if (team.isBanned(p)) {
			return new CommandResponse(p, "admin.join.banned", team.getMiniDisplayName());
		}

		int limit = team.getTeamLimit();

		if (limit > 0 && limit <= team.getMembers().size()) {
			return new CommandResponse(p, "admin.join.full", team.getMiniDisplayName());
		}

		if (team.join(p)) {
			MessageManager.sendMessage(p, "admin.join.notify", team.getMiniDisplayName());
			return new CommandResponse(true, p, "admin.join.success", team.getMiniDisplayName());
		}
		return new CommandResponse(p, "admin.cancel", team.getMiniDisplayName());
	}

	@Override
	public String getCommand() {
		return "join";
	}

	@Override
	public String getNode() {
		return "admin.join";
	}

	@Override
	public String getHelp() {
		return "Force a player to join a team";
	}

	@Override
	public String getArguments() {
		return "<team> <player>";
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
			addPlayerStringList(options, args[1]);
		}
	}

}
