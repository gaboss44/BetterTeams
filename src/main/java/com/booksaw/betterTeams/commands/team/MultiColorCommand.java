package com.booksaw.betterTeams.commands.team;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.booksaw.betterTeams.CommandResponse;
import com.booksaw.betterTeams.Main;
import com.booksaw.betterTeams.PlayerRank;
import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.TeamPlayer;
import com.booksaw.betterTeams.commands.presets.TeamSubCommand;
import com.booksaw.betterTeams.exceptions.InvalidRgbColorCodeException;
import com.booksaw.betterTeams.message.ReferencedFormatMessage;
import com.booksaw.betterTeams.team.MultiColor;

public class MultiColorCommand extends TeamSubCommand {

    private final int limit;
    private final int hardLimit = 50;

	private final boolean lenientParse = Main.plugin.getConfig().getBoolean("multicolor.lenientParse", false);

    public MultiColorCommand() {
        limit = Math.max(Math.min(Main.plugin.getConfig().getInt("multicolor.limit", 3), hardLimit), 0);
    }

    @Override
    public CommandResponse onCommand(TeamPlayer player, String label, String[] args, Team team) {
		if (args.length == 0) {
			MultiColor multiColor = team.getMultiColor();
			if (multiColor == null || multiColor.size() == 0) {
				return new CommandResponse(true, "multicolor.noColor");
			}
			return new CommandResponse(true);
		}

		if (!player.getPlayer().getPlayer().hasPermission("betterteams.multicolor.change")) {
			return new CommandResponse("multicolor.noPerm");
		}

        if (args[0].equalsIgnoreCase("none")) {
            if (!team.setMultiColor(MultiColor.empty())) {
                return new CommandResponse("multicolor.cancel");
            }
            return new CommandResponse(true, "multicolor.success");
        }

        if (args.length > limit) {
            return new CommandResponse("multicolor.limit");
        }

		MultiColor multiColor;
		try {
			multiColor = MultiColor.of(lenientParse, args);
		} catch (InvalidRgbColorCodeException e) {
			return new CommandResponse(new ReferencedFormatMessage("multicolor.fail", e.getInvalid()));
		} 
		
        if (multiColor.size() == 0) {
            return new CommandResponse("multicolor.lenientFail");
        }

        if (!team.setMultiColor(multiColor)) {
            return new CommandResponse("multicolor.cancel");
        }

        return new CommandResponse(true, "multicolor.success");
    }

    @Override
    public PlayerRank getDefaultRank() {
        return PlayerRank.OWNER;
    }

    @Override
    public String getCommand() {
        return "multicolor";
    }

    @Override
    public String getNode() {
        return "multicolor";
    }

    @Override
    public String getHelp() {
        return "Change the team's multicolor";
    }

    @Override
    public String getArguments() {
        return "[rrggbb] [rrggbb] [rrggbb] ...";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public int getMaximumArguments() {
        return limit;
    }

    @Override
    public void onTabComplete(List<String> options, CommandSender sender, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].isEmpty()) {
                options.add("rrggbb rrggbb rrggbb ...");
            }
            if ("none".startsWith(args[0].toLowerCase())) {
                options.add("none");
            }
        }
    }

}