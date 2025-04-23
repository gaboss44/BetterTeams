package com.booksaw.betterTeams.commands.teama;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.booksaw.betterTeams.CommandResponse;
import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.color.MultiColor;
import com.booksaw.betterTeams.commands.presets.TeamSelectSubCommand;

public class MultiColorTeama extends TeamSelectSubCommand {
    
    private final int hardLimit = 50;


    @Override
    public CommandResponse onCommand(CommandSender sender, String label, String[] args, Team team) {
        if (args.length == 2 && args[1].equalsIgnoreCase("none")) {
            if (!team.setMultiColor(new MultiColor())) {
                return new CommandResponse("multicolor.cancelled", team.getMiniDisplayName());
            }
            return new CommandResponse(true, "admin.multicolor.success", team.getMiniDisplayName());
        }

        MultiColor multiColor = new MultiColor(Arrays.copyOfRange(args, 1, args.length));

        if (multiColor.isEmpty()) {
            return new CommandResponse("multicolor.fail", team.getMiniDisplayName());
        }

        if (!team.setMultiColor(multiColor)) {
            return new CommandResponse("multicolor.cancelled", team.getMiniDisplayName());
        }

        return new CommandResponse(true, "admin.multicolor.success", team.getMiniDisplayName());
    }

    @Override
    public String getCommand() {
        return "multicolor";
    }

    @Override
    public String getNode() {
        return "admin.multicolor";
    }

    @Override
    public String getHelp() {
        return "Change that team's multicolor";
    }

    @Override
    public String getArguments() {
        return "<team> [rrggbb] [rrggbb] [rrggbb] ...";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }

    @Override
    public int getMaximumArguments() {
        return hardLimit + 1;
    }

    @Override
    public void onTabComplete(List<String> options, CommandSender sender, String label, String[] args) {
        if (args.length == 2) {
            if (args[1].isEmpty()) {
                options.add("rrggbb rrggbb rrggbb ...");
            }
            if ("none".startsWith(args[1].toLowerCase())) {
                options.add("none");
            }
        } else if (args.length == 1) {
			addTeamStringList(options, args[0]);
		}
    }

}
