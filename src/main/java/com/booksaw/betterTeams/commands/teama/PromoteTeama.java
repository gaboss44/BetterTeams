package com.booksaw.betterTeams.commands.teama;

import com.booksaw.betterTeams.*;
import com.booksaw.betterTeams.commands.SubCommand;
import com.booksaw.betterTeams.message.MessageManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Objects;

public class PromoteTeama extends SubCommand {

	@Override
	public CommandResponse onCommand(CommandSender sender, String label, String[] args) {

		OfflinePlayer p = Utils.getOfflinePlayer(args[0]);
		if (p == null) {
			return new CommandResponse("noPlayer");
		}

		Team team = Team.getTeam(p);
		if (team == null) {
			return new CommandResponse(p, "admin.inTeam");
		}

		TeamPlayer tp = team.getTeamPlayer(p);
		if (Objects.requireNonNull(tp).getRank() == PlayerRank.OWNER) {
			return new CommandResponse(p, "admin.promote.max", team.getMiniDisplayName());
		} else if (tp.getRank() == PlayerRank.ADMIN && Main.plugin.getConfig().getBoolean("singleOwner")) {
			return new CommandResponse(p, "admin.promote.owner", team.getMiniDisplayName());
		}

		if (tp.getRank() == PlayerRank.DEFAULT && team.isMaxAdmins()) {
			return new CommandResponse(p, "admin.promote.maxAdmins", team.getMiniDisplayName());
		} else if (tp.getRank() == PlayerRank.ADMIN && team.isMaxOwners()) {
			return new CommandResponse(p, "admin.promote.maxOwners", team.getMiniDisplayName());
		}

		team.promotePlayer(tp);
		if (p.isOnline()) {
			MessageManager.sendMessage(p.getPlayer(), "admin.promote.notify", team.getMiniDisplayName());
		}
		return new CommandResponse(true, p, "admin.promote.success", team.getMiniDisplayName());
	}

	@Override
	public String getCommand() {
		return "promote";
	}

	@Override
	public String getNode() {
		return "admin.promote";
	}

	@Override
	public String getHelp() {
		return "Promote that player within their team";
	}

	@Override
	public String getArguments() {
		return "<player>";
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
	public void onTabComplete(List<String> options, CommandSender sender, String label, String[] args) {
		if (args.length == 1) {
			addPlayerStringList(options, args[0]);
		}
	}

}
