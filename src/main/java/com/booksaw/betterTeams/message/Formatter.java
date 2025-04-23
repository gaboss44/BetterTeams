package com.booksaw.betterTeams.message;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.booksaw.betterTeams.Main;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;

public class Formatter {

    private static final String MINITAG_CLOSER = "/";

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern LEGACY_TAG_PATTERN = Pattern.compile("[§&]([0-9a-fk-orxA-FK-ORX])");
    private static final Pattern LEGACY_HEX_PATTERN = Pattern.compile("§x(§[0-9a-fA-F]){6}");
    private static final Pattern MULTICOLOR_PATTERN = Pattern.compile("^[A-Fa-f0-9]{6}$");

    private static final MiniMessage MM = MiniMessage.miniMessage();

    /**
     * Translates RGB color codes (&#RRGGBB) into Minecraft color codes.
     *
     * @param message The message to translate.
     * @return The translated message with RGB colors applied.
     */
    public static @NotNull String translateRGBColors(@Nullable String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }

        Matcher matcher = HEX_PATTERN.matcher(message);

        StringBuffer translatedMessage = new StringBuffer();

        while (matcher.find()) {
            String hexColor = matcher.group(1); // Extract the RRGGBB part
            String minecraftColor = ChatColor.of("#" + hexColor).toString(); // Convert to Minecraft color
            matcher.appendReplacement(translatedMessage, minecraftColor);
        }

        matcher.appendTail(translatedMessage);
        return translatedMessage.toString();
    }

    public static @NotNull String legacyTagToMinimessage(@Nullable ChatColor color) {
        return legacyTagToMinimessage(color, false);
    }

    public static @NotNull String legacyTagToMinimessage(@Nullable ChatColor color, boolean closer) {
        if (color == null) {
            return "";
        }

        switch (color.getName().toLowerCase()) {
            case "black":
                return "<" + (closer ? MINITAG_CLOSER : "") + "black>";
            case "dark_blue":
                return "<" + (closer ? MINITAG_CLOSER : "") + "dark_blue>";
            case "dark_green":
                return "<" + (closer ? MINITAG_CLOSER : "") + "dark_green>";
            case "dark_aqua":
                return "<" + (closer ? MINITAG_CLOSER : "") + "dark_aqua>";
            case "dark_red":
                return "<" + (closer ? MINITAG_CLOSER : "") + "dark_red>";
            case "dark_purple":
                return "<" + (closer ? MINITAG_CLOSER : "") + "dark_purple>";
            case "gold":
                return "<" + (closer ? MINITAG_CLOSER : "") + "gold>";
            case "gray":
                return "<" + (closer ? MINITAG_CLOSER : "") + "gray>";
            case "dark_gray":
                return "<" + (closer ? MINITAG_CLOSER : "") + "dark_gray>";
            case "blue":
                return "<" + (closer ? MINITAG_CLOSER : "") + "blue>";
            case "green":
                return "<" + (closer ? MINITAG_CLOSER : "") + "green>";
            case "aqua":
                return "<" + (closer ? MINITAG_CLOSER : "") + "aqua>";
            case "red":
                return "<" + (closer ? MINITAG_CLOSER : "") + "red>";
            case "light_purple":
                return "<" + (closer ? MINITAG_CLOSER : "") + "light_purple>";
            case "yellow":
                return "<" + (closer ? MINITAG_CLOSER : "") + "yellow>";
            case "white":
                return "<" + (closer ? MINITAG_CLOSER : "") + "white>";
            case "obfuscated":
                return "<" + (closer ? MINITAG_CLOSER : "") + "obfuscated>";
            case "bold":
                return "<" + (closer ? MINITAG_CLOSER : "") + "bold>";
            case "strikethrough":
                return "<" + (closer ? MINITAG_CLOSER : "") + "strikethrough>";
            case "underline":
                return "<" + (closer ? MINITAG_CLOSER : "") + "underlined>";
            case "italic":
                return "<" + (closer ? MINITAG_CLOSER : "") + "italic>";
            case "reset":
                return "<reset>"; // reset does not have an end tag
            default:
                return "";
        }
    }

    public static @NotNull String legacyTagToMinimessage(@Nullable String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }
        Matcher legacyMatcher = LEGACY_TAG_PATTERN.matcher(message);
        StringBuffer convertedMessage = new StringBuffer();

        while (legacyMatcher.find()) {
            char code = legacyMatcher.group(1).toLowerCase().charAt(0); // Get the character after § or &
            ChatColor chatColor = ChatColor.getByChar(code); // Get the ChatColor associated with the code

            if (chatColor != null) {
                String miniMessageTag = legacyTagToMinimessage(chatColor); // Get the MiniMessage tag
                legacyMatcher.appendReplacement(convertedMessage, miniMessageTag);
            }
        }
        legacyMatcher.appendTail(convertedMessage);

        return convertedMessage.toString();
    }

    public static @NotNull String legacyHexToMinimessage(@Nullable String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }
        // Convert Minecraft's hex color format (§x§R§R§G§G§B§B) to MiniMessage format
        // (<color:#RRGGBB>)
        Matcher hexMatcher = LEGACY_HEX_PATTERN.matcher(message);
        StringBuffer convertedMessage = new StringBuffer();

        while (hexMatcher.find()) {
            // Extract the hex color from the matched format
            String hexColor = hexMatcher.group().replace("§x", "").replace("§", "");
            hexMatcher.appendReplacement(convertedMessage, "<color:#" + hexColor + ">");
        }
        hexMatcher.appendTail(convertedMessage);

        // Update the message with the converted hex colors
        return convertedMessage.toString();
    }

    /**
     * Transforms legacy formatting codes (§ or &) and Minecraft's hex color format
     * (§x§R§R§G§G§B§B)
     * into MiniMessage-compatible tags.
     *
     * @param message The message to transform.
     * @return The transformed message with MiniMessage-compatible tags.
     */
    public static @NotNull String legacyToMinimessage(@Nullable String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }

        message = legacyHexToMinimessage(message);
        message = legacyTagToMinimessage(message);

        return message;
    }

    public static @NotNull String legacyTranslate(@Nullable String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }
        return translateRGBColors(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static @NotNull String absoluteTranslate(@Nullable String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }
        return legacyToMinimessage(legacyTranslate(message));
    }

    /**
     * Converts a message to a Component using this class' MiniMessage instance.
     *
     * @param message The message to format.
     * @return The formatted Component.
     */
    public static @NotNull Component deserializeWithMiniMessage(@Nullable String message) {
        if (message == null || message.isEmpty()) {
            return Component.empty();
        }
        return MM.deserialize(message);
    }

    public static @NotNull Component absoluteMinimessage(@Nullable String message) {
        if (message == null || message.isEmpty()) {
            return Component.empty();
        }
        return MM.deserialize(absoluteTranslate(message));
    }

    public static @NotNull String legacySerialize(@Nullable Component component) {
        if (component == null || component.equals(Component.empty())) {
            return "";
        }
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public static @NotNull String legacySerialize(@Nullable String string) {
        return legacySerialize(string, true);
    }

    public static @NotNull String legacySerialize(@Nullable String string, boolean translate) {
        if (string == null || string.isEmpty()) {
            return "";
        }

        if (translate) {
            string = absoluteTranslate(string);
        }

        return legacySerialize(deserializeWithMiniMessage(string));
    }

    public static @NotNull String setPlaceholders(@Nullable String text, @Nullable Player player) {
        return setPlaceholders(text, (OfflinePlayer) player);
    }

    public static @NotNull String setPlaceholders(@Nullable String text, @Nullable OfflinePlayer player) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        if (player == null || !Main.placeholderAPI) {
            return text;
        }
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    public static @NotNull String setPlaceholders(@Nullable String text, @Nullable Object... replacement) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        if (replacement == null || replacement.length == 0) {
            return text;
        }

        StringBuilder formatted = new StringBuilder(text);
        for (int i = 0; i < replacement.length; i++) {
            if (replacement[i] != null) {
                String placeholder = "{" + i + "}";
                int index;
                while ((index = formatted.indexOf(placeholder)) != -1) {
                    formatted.replace(index, index + placeholder.length(), replacement[i].toString());
                }
            }
        }
        return formatted.toString();
    }

    public static @NotNull String parseMultiColor(@Nullable String... args) throws IllegalArgumentException {
        if (args == null || args.length == 0) {
            return "";
        }

        List<String> validColors = new ArrayList<>();

        for (String group : args) {
            if (MULTICOLOR_PATTERN.matcher(group).matches()) {
                validColors.add(group.toUpperCase());
            } else {
                throw new IllegalArgumentException("Invalid color code: " + group);
            }
        }

        if (validColors.isEmpty()) {
            return "";
        } else if (validColors.size() == 1) {
            return "<color:#" + validColors.get(0) + ">";
        } else {
            return "<gradient:"
                    + String.join(":", validColors.stream().map(color -> "#" + color).toArray(String[]::new)) + ">";
        }
    }

    public static @NotNull String parseMultiColor(@Nullable String text) throws IllegalArgumentException {
        if (text == null || text.isEmpty()) {
            return "";
        }

        return parseMultiColor(text.split(","));
    }
}
