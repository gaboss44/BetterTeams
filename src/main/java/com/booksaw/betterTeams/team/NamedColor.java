package com.booksaw.betterTeams.team;

import java.util.Map;

import org.bukkit.ChatColor;

import com.booksaw.betterTeams.exceptions.InvalidChatColorException;
import com.booksaw.betterTeams.exceptions.UnrecognizedChatColorException;

import com.google.common.collect.ImmutableMap;

public final class NamedColor {

	private static final Map<String, ChatColor> VALID_COLORS = ImmutableMap.<String, ChatColor>builder()
			.put("black", ChatColor.BLACK)
			.put("dark_blue", ChatColor.DARK_BLUE)
			.put("dark_green", ChatColor.DARK_GREEN)
			.put("dark_aqua", ChatColor.DARK_AQUA)
			.put("dark_red", ChatColor.DARK_RED)
			.put("dark_purple", ChatColor.DARK_PURPLE)
			.put("gold", ChatColor.GOLD)
			.put("gray", ChatColor.GRAY)
			.put("dark_gray", ChatColor.DARK_GRAY)
			.put("blue", ChatColor.BLUE)
			.put("green", ChatColor.GREEN)
			.put("aqua", ChatColor.AQUA)
			.put("red", ChatColor.RED)
			.put("light_purple", ChatColor.LIGHT_PURPLE)
			.put("yellow", ChatColor.YELLOW)
			.put("white", ChatColor.WHITE)
			.build();

	private static ChatColor validateArg(char arg) throws UnrecognizedChatColorException {
		ChatColor color = ChatColor.getByChar(arg);
		if (color == null) {
			throw new UnrecognizedChatColorException("Unrecognized color code: " + arg, String.valueOf(arg));
		}
		return validateArg(color);
	}

	private static ChatColor validateArg(String arg) throws UnrecognizedChatColorException, InvalidChatColorException {
		if (!VALID_COLORS.containsKey(arg)) {
			throw new UnrecognizedChatColorException("Unrecognized color name: " + arg, arg);
		}
		return VALID_COLORS.get(arg);
	}

	private static ChatColor validateArg(ChatColor arg) throws InvalidChatColorException {
		if (arg == null) {
			throw new InvalidChatColorException("Invalid color code: " + arg, arg);
		}
		return arg;
	}
}
