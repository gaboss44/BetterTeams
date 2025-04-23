package com.booksaw.betterTeams.commands.teama;

import com.booksaw.betterTeams.CommandResponse;
import com.booksaw.betterTeams.Main;
import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.TeamPlayer;
import com.booksaw.betterTeams.commands.SubCommand;
import com.booksaw.betterTeams.message.Formatter;
import com.booksaw.betterTeams.message.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TitleTeama extends SubCommand {

	@Override
	public CommandResponse onCommand(CommandSender sender, String label, String[] args) {
		Team team = Team.getTeam(Bukkit.getPlayer(args[0]));

		if (team == null) {
			return new CommandResponse("admin.inTeam");
		}

		Player p = Bukkit.getPlayer(args[0]);
		TeamPlayer toTitle = team.getTeamPlayer(p);

		if (toTitle == null) {
			return new CommandResponse("noPlayer", team.getMiniDisplayName());
		}

		if (args.length == 1) {
			team.setTitle(toTitle, "");
			MessageManager.sendMessage(p, "admin.title.reset", team.getMiniDisplayName());
			return new CommandResponse(true, p, "title.success", team.getMiniDisplayName());
		}

		if (args[1].length() > Main.plugin.getConfig().getInt("maxTitleLength")) {
			return new CommandResponse(p, "title.tooLong", team.getMiniDisplayName());
		}

		if (!Team.isValidTeamName(args[1])) {
			return new CommandResponse(p, "bannedChar", team.getMiniDisplayName());
		}

		args[1] = Formatter.legacyTranslate(args[1]);

		team.setTitle(toTitle, args[1]);

		MessageManager.sendMessage(p, "title.change", args[1], team.getMiniDisplayName());

		return new CommandResponse(true, p, "admin.title.success", args[1], team.getMiniDisplayName());
	}

	@Override
	public String getCommand() {
		return "title";
	}

	@Override
	public String getNode() {
		return "admin.title";
	}

	@Override
	public String getHelp() {
		return "Change a players title to the specified title";
	}

	@Override
	public String getArguments() {
		return "<player> <title>";
	}

	@Override
	public int getMinimumArguments() {
		return 1;
	}

	@Override
	public int getMaximumArguments() {
		return 2;
	}

	@Override
	public void onTabComplete(List<String> options, CommandSender sender, String label, String[] args) {
		if (args.length == 1) {
			addPlayerStringList(options, args[0]);
		}
		if (args.length == 2) {
			options.add("<title>");
		}
	}

}
