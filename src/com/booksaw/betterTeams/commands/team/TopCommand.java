package com.booksaw.betterTeams.commands.team;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.booksaw.betterTeams.MessageManager;
import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.commands.SubCommand;

public class TopCommand extends SubCommand {

	@Override
	public String onCommand(CommandSender sender, String label, String[] args) {

		Team team = null;
		boolean contained = false;

		if (sender instanceof Player) {
			team = Team.getTeam((Player) sender);
		}

		Team[] teams = Team.sortTeams();
		sender.sendMessage(MessageManager.getMessage("top.leaderboard"));

		for (int i = 0; i < 10 && i < teams.length; i++) {
			sender.sendMessage(String.format(MessageManager.getMessage("top.syntax"), (i + 1) + "", teams[i].getName(),
					teams[i].getScore()));
			if (team == teams[i]) {
				contained = true;
			}
		}

		if (!contained && team != null) {
			try {
				int rank = 0;
				for (int i = 10; i < teams.length; i++) {
					if (teams[i] == team) {
						rank = i + 1;
						break;
					}
				}
				if (rank != 0) {
					MessageManager.sendMessage(sender, "top.divide");
					if (rank - 2 > 9) {
						sender.sendMessage(String.format(MessageManager.getMessage("top.syntax"), (rank - 1) + "",
								teams[rank - 2].getName(), teams[rank - 2].getScore()));
					}

					sender.sendMessage(String.format(MessageManager.getMessage("top.syntax"), rank + "", team.getName(),
							team.getScore()));

					if (teams.length > rank) {
						sender.sendMessage(String.format(MessageManager.getMessage("top.syntax"), (rank + 1) + "",
								teams[rank].getName(), teams[rank].getScore()));
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				// to save an additional check on arrays length
			}
		}

		return null;
	}

	@Override
	public String getCommand() {
		return "top";
	}

	@Override
	public String getNode() {
		return "top";
	}

	@Override
	public String getHelp() {
		return "View the top teams";
	}

	@Override
	public String getArguments() {
		return null;
	}

	@Override
	public int getMinimumArguments() {
		return 0;
	}

	@Override
	public int getMaximumArguments() {
		return 0;
	}

	@Override
	public void onTabComplete(List<String> options, CommandSender sender, String label, String[] args) {
	}

}
