package com.booksaw.betterTeams.text;

import java.util.Locale;
import java.util.Map;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableMap;

public abstract class ChatColorWrapper {

	private static final Map<String, ChatColor> CHATCOLOR_BY_NAME_MAP = ImmutableMap.<String, ChatColor>builder()
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
			.put("reset", ChatColor.RESET)
			.put("bold", ChatColor.BOLD)
			.put("italic", ChatColor.ITALIC)
			.put("underline", ChatColor.UNDERLINE)
			.put("strikethrough", ChatColor.STRIKETHROUGH)
			.put("obfuscated", ChatColor.MAGIC)
			.put("magic", ChatColor.MAGIC)
			.build();

	protected static @Nullable ChatColor getByName(String name) {
		return CHATCOLOR_BY_NAME_MAP.get(name.toLowerCase(Locale.ROOT));
	}
}
