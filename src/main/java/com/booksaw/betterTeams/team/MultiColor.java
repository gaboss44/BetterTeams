package com.booksaw.betterTeams.team;

import java.util.Arrays;
import java.util.regex.Pattern;

import com.booksaw.betterTeams.exceptions.InvalidRgbColorCodeException;

import lombok.Getter;

public final class MultiColor {

	private static final Pattern HEX_PATTERN = Pattern.compile("(?i)^[0-9A-F]{6}$");

	private static final String OPEN_GRADIENT = "<gradient:";
	private static final String CLOSE_GRADIENT = "</gradient>";
	private static final String OPEN_COLOR = "<c:";
	private static final String CLOSE_COLOR = "</c>";
	private static final String CLOSE_TAG_CHAR = ">";
	private static final String HEX_CHAR = "#";

	private static final MultiColor EMPTY = new MultiColor();

	public static MultiColor empty() { return EMPTY; }

	public static MultiColor fromString(String string) {
		return fromString(string, ",", false);
	}

	public static MultiColor fromString(String string, boolean lenient) {
		return fromString(string, ",", lenient);
	}

	public static MultiColor fromString(String string, String spliter) {
		return fromString(string, spliter, false);
	}

	public static MultiColor fromString(String string, String spliter, boolean lenient) {
		return of(lenient, string.split(spliter));
	}

	public static MultiColor of(String... args) {
		return of(false, args);
	}

	public static MultiColor of(boolean lenient, String... args) {
		return new MultiColor(lenient ? filterValidArgs(args) : validateArgs(args));
	}

	private static String[] validateArgs(String... args) throws InvalidRgbColorCodeException {
		for (String arg : args) {
			if (!isValidArg(arg)) {
				throw new InvalidRgbColorCodeException("Invalid hex color code: " + arg, arg);
			}
		}
		return args.clone();
	}

	private static String[] filterValidArgs(String... args) {
		return Arrays.stream(args).filter(arg -> isValidArg(arg)).toArray(String[]::new);
	}

	public static boolean isValidArg(String hex) {
		return HEX_PATTERN.matcher(hex).matches();
	}

	private static String toGradient(String... args) {
		StringBuilder sb = new StringBuilder(OPEN_GRADIENT);
		for (int i = 0; i < args.length; i++) {
			sb.append(HEX_CHAR).append(args[i]);
			if (i != args.length - 1) {
				sb.append(':');
			}
		}
		sb.append(CLOSE_TAG_CHAR);
		return sb.toString();
	}

	private final String[] args;

	public int size() {
		return args.length;
	}

	public String getArg(int index) {
		if (index < 0 || index >= args.length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + args.length);
		}
		return args[index];
	}

	public String[] getArgs() {
		return args.clone();
	}

	public String join() {
		return join(",");
	}

	private String join(String spliter) {
		return String.join(spliter, args);
	}

	@Getter
	private final String openTag;

	public String toString() {
		return openTag;
	}

	@Getter
	public final String closeTag;

	private MultiColor() {
		args = new String[0];
		openTag = "";
		closeTag = "";
	}

	private MultiColor(String... args) {
		if (args.length == 0) {
			this.args = new String[0];
			openTag = "";
			closeTag = "";
		} else if (args.length == 1) {
			this.args = args;
			openTag = OPEN_COLOR + HEX_CHAR + args[0] + CLOSE_TAG_CHAR;
			closeTag = CLOSE_COLOR;
		} else {
			this.args = args;
			openTag = toGradient(args);
			closeTag = CLOSE_GRADIENT;
		}
	}
}
