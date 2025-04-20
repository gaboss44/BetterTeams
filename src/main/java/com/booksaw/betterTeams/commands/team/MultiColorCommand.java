package com.booksaw.betterTeams.commands.team;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.booksaw.betterTeams.CommandResponse;
import com.booksaw.betterTeams.Main;
import com.booksaw.betterTeams.PlayerRank;
import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.TeamPlayer;
import com.booksaw.betterTeams.color.MultiColor;
import com.booksaw.betterTeams.commands.presets.TeamSubCommand;

public class MultiColorCommand extends TeamSubCommand {

    private final int limit;
    private final int defaultLimit = 5;
    private final int hardLimit = 50;

    public MultiColorCommand() {
        limit = Math.min(Main.plugin.getConfig().getInt("multicolor.limit", defaultLimit), hardLimit);
    }

    @Override
    public CommandResponse onCommand(TeamPlayer player, String label, String[] args, Team team) {
        if (args.length == 1 && args[0].equalsIgnoreCase("none")) {
            if (!team.setMultiColor(new MultiColor())) {
                return new CommandResponse("multicolor.cancelled");
            }
            return new CommandResponse(true, "multicolor.success");
        }

        if (args.length > limit) {
            return new CommandResponse("multicolor.limit");
        }

        MultiColor multiColor = new MultiColor(args);

        if (multiColor.isEmpty()) {
            return new CommandResponse("multicolor.fail");
        }

        if (!team.setMultiColor(multiColor)) {
            return new CommandResponse("multicolor.cancelled");
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
        return "Change the team's multi-color";
    }

    @Override
    public String getArguments() {
        return "[rrggbb] [rrggbb] [rrggbb] ...";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public int getMaximumArguments() {
        return hardLimit;
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
