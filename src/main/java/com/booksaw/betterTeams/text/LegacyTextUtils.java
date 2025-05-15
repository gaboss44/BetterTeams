package com.booksaw.betterTeams.text;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class LegacyTextUtils {

	private LegacyTextUtils() {}

	static final char SECTION = '\u00A7';
	static final char AMPERSAND = '&';

    private static final Pattern MOJANG_COLOR_PATTERN = Pattern.compile("(?i)&([0-9A-FK-OR])");
    private static final Pattern STANDARD_HEX_PATTERN = Pattern.compile("(?i)&#([0-9A-F]{6})");
    private static final Pattern BUNGEE_HEX_PATTERN = Pattern.compile("(?i)&x(&[0-9A-F]){6}");

	public static String sectionToAmpersand(String input) {
		return input.replace(SECTION, AMPERSAND);
	}

	public static String bungeeHexToStandardHex(String input) {
		Matcher matcher = BUNGEE_HEX_PATTERN.matcher(input);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String hexColor = matcher.group().replace("&x", "").replace("&", "");
            matcher.appendReplacement(buffer, "&#" + hexColor.toUpperCase(Locale.ROOT));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
	}

	public static String bungeeHexToAdventure(String input) {
		Matcher matcher = BUNGEE_HEX_PATTERN.matcher(input);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String hexColor = matcher.group().replace("&x", "").replace("&", "");
            matcher.appendReplacement(buffer, "<c:#" + hexColor.toUpperCase(Locale.ROOT) + ">");
        }
        matcher.appendTail(buffer);
        return buffer.toString();
	}

	public static String standardHexToAdventure(String input) {
		Matcher matcher = STANDARD_HEX_PATTERN.matcher(input);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, "<c:#" + matcher.group(1) + ">");
        }
        matcher.appendTail(buffer);
        return buffer.toString();
	}

	public static String colorToAdventure(ChatColor color) {
		return colorToAdventure(color, false);
	}

    public static String colorToAdventure(ChatColor color, boolean close) {
        if (color == null) {
            return "";
        }

		TextStyle.Tag tag = TextStyle.BukkitTag.get(color);
		if (close) {
			return tag.close();
		} else {
			return tag.open();
		}
	}

	public static String colorToAdventure(String input, boolean close) {
		if (input == null || input.isEmpty()) return "";
		if (close) {
			return LegacyComponentSerializer.legacySection().serializeOr(Formatter.absolute().process(input), "");
		} else {
			return LegacyComponentSerializer.legacySection().serializeOr(Formatter.legacy().process(input), "");
		}
    }

    public static String colorToAdventure(String input) {
		Matcher matcher = MOJANG_COLOR_PATTERN.matcher(input);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            ChatColor color = ChatColor.getByChar(matcher.group(1));
            if (color != null) {
                matcher.appendReplacement(buffer, colorToAdventure(color));
            }
        }
        matcher.appendTail(buffer);
		return buffer.toString();
    }

	public static String toAdventure(String input) {
		return toAdventure(input, true, true, true, true);
	}

	public static String toAdventure(String input, boolean sectionToAmpersand, boolean bungeeHex, boolean standardHex, boolean chatColor) {
		if (input == null || input.isEmpty()) return "";
		String output = input;
		if (sectionToAmpersand) output = sectionToAmpersand(output);
		if (bungeeHex) output = bungeeHexToAdventure(output);
		if (standardHex) output = standardHexToAdventure(output);
		if (chatColor) output = colorToAdventure(output);
		return output;
	}

	public static String parseAllAdventure(String input) {
		return LegacyComponentSerializer.legacySection().serializeOr(Formatter.absolute().process(input), "");
	}

	public static String parseAdventure(String input) {
		return LegacyComponentSerializer.legacySection().serializeOr(Formatter.legacy().process(input), "");
	}

	public static String serialize(Component input) {
		return LegacyComponentSerializer.legacySection().serializeOr(input, "");
	}
}
