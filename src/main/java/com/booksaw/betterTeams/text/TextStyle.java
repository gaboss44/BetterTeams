package com.booksaw.betterTeams.text;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

import com.booksaw.betterTeams.exceptions.InvalidArgumentException;

import lombok.Getter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class TextStyle {

	private static final Map<ChatColor, BukkitTag> BY_CHATCOLOR = new HashMap<>();

	private static final Map<String, BukkitTag> BY_NAME = new HashMap<>();

	private TextStyle() {
	}

	private static interface Permitted {
	}

	public static interface Tag {

		String value();

		default String open() {
			return "<" + value() + ">";
		}

		default String close() {
			return "</" + value() + ">";
		}

		default boolean isPermitted() {
			return this instanceof Permitted;
		}

	}

	static interface ArgumentedTag extends Tag {

		Collection<String> args();

		@Override
		default String open() {
			StringBuilder sb = new StringBuilder("<" + value());
			for (String arg : args()) {
				sb.append(":").append(arg);
			}
			sb.append(">");
			return sb.toString();
		}

	}

	public static interface ColorTag extends Tag {

		static ColorTag of(String color) {
			Optional<ColorTag> tag = ofNullable(color);
			if (tag.isPresent()) {
				return tag.get();
			} else {
				throw new IllegalArgumentException("Invalid color: " + color);
			}
		}

		static Optional<ColorTag> ofNullable(String color) {
			if (color == null || color.isEmpty()) {
				throw new IllegalArgumentException("Color cannot be " + (color == null ? "null" : "empty"));
			}
			Tag tag = BukkitTag.get(color);
			if (tag != null && tag instanceof ColorTag) {
				return Optional.of((ColorTag) tag);
			}
			if (HexColorTag.HEX_PATTERN.matcher(color).matches()) {
				return Optional.of(new HexColorTag(color));
			} else if (color.startsWith(HexColorTag.HEX_PREFIX)) {
				String hex = color.substring(HexColorTag.HEX_PREFIX.length());
				if (HexColorTag.HEX_PATTERN.matcher(hex).matches()) {
					return Optional.of(new HexColorTag(hex));
				}
			}
			return Optional.empty();
		}
	}

	public static final class HexColorTag implements ColorTag, Permitted {

		public static final String HEX_PREFIX = "#";

		public static final Pattern HEX_PATTERN = Pattern.compile("(?i)[0-9A-F]{6}");

		public static HexColorTag of(String hex) {
			if (hex == null || hex.isEmpty()) {
				throw new IllegalArgumentException("Hex color cannot be " + (hex == null ? "null" : "empty"));
			}
			if (!HEX_PATTERN.matcher(hex).matches()) {
				throw new IllegalArgumentException("Invalid hex color: " + hex);
			}
			return new HexColorTag(hex);
		}

		@Getter
		private final String hex;

		private HexColorTag(String hex) {
			this.hex = hex;
		}

		@Override
		public String value() {
			return HEX_PREFIX + hex;
		}
	}

	public static final class MultiColorTag implements ColorTag, ArgumentedTag, Permitted {

		@Getter
		private final Collection<ColorTag> colors;

		private final Collection<String> args;

		@Override
		public Collection<String> args() {
			return args;
		}

		public static MultiColorTag of(ColorTag... colors) {
			if (colors == null || colors.length == 0) {
				throw new IllegalArgumentException("Colors cannot be " + (colors == null ? "null" : "empty"));
			}
			Collection<ColorTag> colorList = new ArrayList<>();
			for (ColorTag color : colors) {
				if (color == null) {
					throw new NullPointerException("Found null color in colors");
				}
				if (color instanceof MultiColorTag) {
					colorList.addAll(((MultiColorTag) color).getColors());
				} else {
					colorList.add(color);
				}
			}
			return new MultiColorTag(colorList);
		}

		public static MultiColorTag of(Collection<ColorTag> colors) {
			if (colors == null || colors.isEmpty()) {
				throw new IllegalArgumentException("Colors cannot be " + (colors == null ? "null" : "empty"));
			}
			Collection<ColorTag> colorList = new ArrayList<>();
			for (ColorTag color : colors) {
				if (color == null) {
					throw new NullPointerException("Found null color in colors");
				}
				if (color.isPermitted()) {
					throw new IllegalArgumentException("Custom ColorTag implementation is not permitted");
				}
				if (color instanceof MultiColorTag) {
					colorList.addAll(((MultiColorTag) color).getColors());
				} else {
					colorList.add(color);
				}
			}
			return new MultiColorTag(colorList);
		}

		private MultiColorTag(Collection<ColorTag> colors) {
			this.colors = colors;
			this.args = colors.stream().map(ColorTag::value).collect(Collectors.toList());
		}

		@Override
		public String value() {
			if (colors.isEmpty()) {
				return ""; // should never happen
			} else if (colors.size() == 1) {
				return "color";
			} else {
				return "gradient";
			}
		}
	}

	public static final class ShadowTag implements Tag, Permitted {

		public static final String SHADOW_PREFIX = "shadow";

		@Getter
		private final ColorTag color;

		public static ShadowTag of(ColorTag color) {
			if (color == null) {
				throw new IllegalArgumentException("Color cannot be null");
			} else if (color instanceof MultiColorTag) {
				throw new IllegalArgumentException("MultiColorTag cannot be used as a ShadowTag");
			}
			return new ShadowTag(color);
		}

		private ShadowTag(ColorTag color) {
			this.color = color;
		}

		@Override
		public String value() {
			return "shadow";
		}
		 
		@Override
		public String open() {
			return "<" + value() + ":" + color.value() + ">";
		}
	}

	public static interface BukkitTag extends Tag {

		String getName();

		ChatColor getChatColor();

		@Override
		default String value() {
			return getName();
		}

		default char getChar() {
			return getChatColor().getChar();
		}

		static BukkitTag get(ChatColor chatColor) {
			if (chatColor == null) {
				throw new IllegalArgumentException("ChatColor cannot be null");
			}
			return BY_CHATCOLOR.get(chatColor);
		}

		static BukkitTag getOr(ChatColor chatColor, @Nullable BukkitTag defaultTag) {
			if (chatColor == null) {
				throw new IllegalArgumentException("ChatColor cannot be null");
			}
			return BY_CHATCOLOR.getOrDefault(chatColor, defaultTag);
		}

		static BukkitTag get(String name) {
			if (name == null || name.isEmpty()) {
				throw new IllegalArgumentException("Name cannot be " + (name == null ? "null" : "empty"));
			}
			if (name.length() == 1) {
				ChatColor chatColor = ChatColor.getByChar(name.charAt(0));
				if (chatColor != null) {
					return BY_CHATCOLOR.get(chatColor);
				}
			}
			BukkitTag tag = BY_NAME.get(name);
			if (tag == null) {
				throw new InvalidArgumentException("Invalid TextStyle.BukkitTag name: " + name, name);
			}
			return tag;
		}

		static BukkitTag get(char c) {
			ChatColor chatColor = ChatColor.getByChar(c);
			if (chatColor == null) {
				throw new InvalidArgumentException("Invalid ChatColor char: " + c, c);
			}
			BukkitTag tag = BY_CHATCOLOR.get(chatColor);
			if (tag == null) {
				throw new InvalidArgumentException("Invalid TextStyle.BukkitTag char: " + c, c);
			}
			return tag;
		}

		static BukkitTag getOr(char c, @Nullable BukkitTag defaultTag) {
			ChatColor chatColor = ChatColor.getByChar(c);
			if (chatColor == null) {
				return defaultTag;
			}
			BukkitTag tag = BY_CHATCOLOR.get(chatColor);
			if (tag == null) {
				return defaultTag;
			}
			return tag;
		}

		static BukkitTag getOr(String name, @Nullable BukkitTag defaultTag) {
			if (name == null || name.isEmpty()) {
				throw new IllegalArgumentException("Name cannot be " + (name == null ? "null" : "empty"));
			}
			return BY_NAME.getOrDefault(name, defaultTag);
		}
	}

	public static enum NamedColorTag implements ColorTag, BukkitTag, Permitted {

		BLACK("black", ChatColor.BLACK),

		DARK_BLUE("dark_blue", ChatColor.DARK_BLUE),

		DARK_GREEN("dark_green", ChatColor.DARK_GREEN),

		DARK_AQUA("dark_aqua", ChatColor.DARK_AQUA),

		DARK_RED("dark_red", ChatColor.DARK_RED),

		DARK_PURPLE("dark_purple", ChatColor.DARK_PURPLE),

		GOLD("gold", ChatColor.GOLD),

		GRAY("gray", ChatColor.GRAY),

		DARK_GRAY("dark_gray", ChatColor.DARK_GRAY),

		BLUE("blue", ChatColor.BLUE),

		GREEN("green", ChatColor.GREEN),

		AQUA("aqua", ChatColor.AQUA),

		RED("red", ChatColor.RED),

		LIGHT_PURPLE("light_purple", ChatColor.LIGHT_PURPLE),

		YELLOW("yellow", ChatColor.YELLOW),

		WHITE("white", ChatColor.WHITE);

		@Getter
		private final String name;

		@Getter
		private final ChatColor chatColor;

		private NamedColorTag(String name, ChatColor chatColor) {
			this.name = name;
			this.chatColor = chatColor;
		}

		static {
			for (NamedColorTag tag : values()) {
				BY_CHATCOLOR.put(tag.chatColor, tag);
				BY_NAME.put(tag.name, tag);
			}
		}
	}

	public static enum DecorationTag implements BukkitTag, Permitted {

		BOLD("bold", "b", ChatColor.BOLD),

		ITALIC("italic", "i", ChatColor.ITALIC),

		UNDERLINED("underlined", "u", ChatColor.UNDERLINE),

		STRIKETHROUGH("strikethrough", "st", ChatColor.STRIKETHROUGH),

		OBFUSCATED("obfuscated", "obf", ChatColor.MAGIC),

		RESET("legacyreset", "lr", ChatColor.RESET);

		@Getter
		private final String name;

		private final String alias;

		@Getter
		private final ChatColor chatColor;

		private DecorationTag(String name, String alias, ChatColor chatColor) {
			this.name = name;
			this.alias = alias;
			this.chatColor = chatColor;
		}

		@Override
		public String value() {
			return alias;
		}

		static {
			for (DecorationTag tag : values()) {
				BY_CHATCOLOR.put(tag.chatColor, tag);
				BY_NAME.put(tag.name, tag);
			}
		}
	}
}
