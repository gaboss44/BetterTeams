package com.booksaw.betterTeams.commands.team;

import com.booksaw.betterTeams.*;
import com.booksaw.betterTeams.commands.presets.TeamSubCommand;
import com.booksaw.betterTeams.message.MessageManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Objects;

public class BanCommand extends TeamSubCommand {

	@Override
	public CommandResponse onCommand(TeamPlayer teamPlayer, String label, String[] args, Team team) {

		/*
		 * method is depreciated as it does not guarantee the expected player, in most
		 * use cases this will work and it will be down to the user if it does not due
		 * to name changes This method is appropriate to use in this use case (so users
		 * can view offline users teams by name not just by team name)
		 */
		OfflinePlayer player = Utils.getOfflinePlayer(args[0]);

		if (player == null) {
			return new CommandResponse("noPlayer");
		}

		Team otherTeam = Team.getTeam(player);

		if (team.isBanned(player)) {
			return new CommandResponse(player, "ban.already");
		}

		if (team != otherTeam) {
			team.banPlayer(player);
			if (player.isOnline()) {
				MessageManager.sendMessage(player.getPlayer(), "ban.notify", team.getName());
			}
			return new CommandResponse(player, "ban.success");
		}

		TeamPlayer kickedPlayer = team.getTeamPlayer(player);

		// ensuring the player they are banning has less perms than them
		if (teamPlayer.getRank().value <= Objects.requireNonNull(kickedPlayer).getRank().value) {
			return new CommandResponse("ban.noPerm");
		}

		// player is in the team, so removing them
		team.removePlayer(player);
		team.banPlayer(player);

		if (player.isOnline()) {
			MessageManager.sendMessage(player.getPlayer(), "ban.notify", team.getName());

			if (Main.plugin.getConfig().getBoolean("titleRemoval")) {
				player.getPlayer().sendTitle(" ", MessageManager.getMessage("ban.title"), 10, 100, 20);
			}
		}

		return new CommandResponse(true, player, "ban.success");
	}

	@Override
	public String getCommand() {
		return "ban";
	}

	@Override
	public int getMinimumArguments() {
		return 1;
	}

	@Override
	public int getMaximumArguments() {
		return 1;
	}

	@Override
	public String getNode() {
		return "ban";
	}

	@Override
	public String getHelp() {
		return "Bans the specified player from your team";
	}

	@Override
	public String getArguments() {
		return "<player>";
	}

	@Override
	public void onTabComplete(List<String> options, CommandSender sender, String label, String[] args) {
		addPlayerStringList(options, (args.length == 0) ? "" : args[0]);
	}

	@Override
	public PlayerRank getDefaultRank() {
		return PlayerRank.ADMIN;
	}

}
