package com.booksaw.betterTeams.commands.teama;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.booksaw.betterTeams.CommandResponse;
import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.commands.presets.TeamSelectSubCommand;

public class StyleTeama extends TeamSelectSubCommand {

    private final Set<Character> banned = Arrays.stream(ChatColor.values())
            .filter(c -> !c.isFormat())
            .map(ChatColor::getChar)
            .collect(Collectors.toSet());

    @Override
    public CommandResponse onCommand(CommandSender sender, String label, String[] args, Team team) {
        if (args.length == 2 && args[1].equalsIgnoreCase("none")) {
            if (!team.setStyle(null)) {
                return new CommandResponse("style.cancelled");
            }
            return new CommandResponse(true, "admin.style.success");
        }

		ChatColor style = null;
		try {
			style = ChatColor.valueOf(args[1].toUpperCase());
		} catch (IllegalArgumentException e) {
			// expected if they do not input a correct value, or a char
		}
		if (style == null) {
			style = ChatColor.getByChar(args[1]);
			if (style == null || args[1].length() > 1)
				return new CommandResponse("style.fail");
		}

		if (banned.contains(style.getChar())) {
			return new CommandResponse("style.banned");
		}

		if (!team.setStyle(style)) {
			return new CommandResponse("style.cancelled");
		}

		return new CommandResponse(true, "admin.style.success");
    }

    @Override
    public String getCommand() {
        return "style";
    }

    @Override
    public String getNode() {
        return "admin.style";
    }

    @Override
    public String getHelp() {
        return "Change that team's style";
    }

    @Override
    public String getArguments() {
        return "[k|l|m|n|o|r|none]";
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
		if (args.length == 2) {
			for (ChatColor c : ChatColor.values()) {
				if (!banned.contains(c.getChar()) && c.name().toLowerCase().startsWith(args[0].toLowerCase())) {
					options.add(c.name().toLowerCase());
				}
			}
		} else if (args.length == 1) {
			addTeamStringList(options, args[0]);
		}
    }

}
