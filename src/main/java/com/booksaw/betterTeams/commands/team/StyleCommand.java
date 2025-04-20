package com.booksaw.betterTeams.commands.team;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.booksaw.betterTeams.CommandResponse;
import com.booksaw.betterTeams.Main;
import com.booksaw.betterTeams.PlayerRank;
import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.TeamPlayer;
import com.booksaw.betterTeams.commands.presets.TeamSubCommand;
import org.bukkit.ChatColor;
import java.util.Set;
import java.util.stream.Collectors;

public class StyleCommand extends TeamSubCommand {

    private final Set<Character> banned = Arrays.stream(ChatColor.values())
            .filter(c -> !c.isFormat())
            .map(ChatColor::getChar)
            .collect(Collectors.toSet());

    public StyleCommand() {
        banned.addAll(Main.plugin.getConfig().getString("bannedStyles").chars().mapToObj(c -> (char) c)
				.collect(Collectors.toList()));
    }

    @Override
    public CommandResponse onCommand(TeamPlayer player, String label, String[] args, Team team) {
        if (args[0].equalsIgnoreCase("none")) {
            if (!team.setStyle(null)) {
                return new CommandResponse("style.cancelled");
            }
            return new CommandResponse(true, "style.success");
        }

		ChatColor style = null;
		try {
			style = ChatColor.valueOf(args[0].toUpperCase());
		} catch (IllegalArgumentException e) {
			// expected if they do not input a correct value, or a char
		}
		if (style == null) {
			style = ChatColor.getByChar(args[0]);
			if (style == null || args[0].length() > 1)
				return new CommandResponse("style.fail");
		}

		if (banned.contains(style.getChar())) {
			return new CommandResponse("style.banned");
		}

		if (!team.setStyle(style)) {
			return new CommandResponse("style.cancelled");
		}

		return new CommandResponse(true, "style.success");
    }

    @Override
    public PlayerRank getDefaultRank() {
        return PlayerRank.OWNER;
    }

    @Override
    public String getCommand() {
        return "style";
    }

    @Override
    public String getNode() {
        return "style";
    }

    @Override
    public String getHelp() {
        return "Change the team's style";
    }

    @Override
    public String getArguments() {
        return "[k|l|m|n|o|r|none]";
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
			for (ChatColor c : ChatColor.values()) {
				if (!banned.contains(c.getChar()) && c.name().toLowerCase().startsWith(args[0].toLowerCase())) {
					options.add(c.name().toLowerCase());
				}
			}
		}
    }

}
