package com.booksaw.betterTeams.color;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.message.Formatter;
import com.booksaw.betterTeams.team.storage.team.StoredTeamValue;

/**
 * A class which represents a multicolor for a {@link Team}
 * <p>
 * This class is used to store the primitive string value of the multicolor
 * and the mini tag which represents the multicolor in the chat.
 */
public class MultiColor implements Comparable<MultiColor>, Serializable, CharSequence {

    /**
     * The primitive string which represent this multicolor's raw value
     */
    public final String primitive;

    /**
     * The mini tag which represent this multicolor's value
     */
    public final String value;

    public final int colorQuantity;

    public MultiColor() {
        this.primitive = "";
        this.value = "";
        this.colorQuantity = 0;
    }

    public MultiColor(@Nullable String... args) {
        if (args == null || args.length == 0) {
            this.primitive = "";
            this.value = "";
            this.colorQuantity = 0;
            return;
        }

        for (String arg : args) {
            if (arg.length() != 6) {
                this.primitive = "";
                this.value = "";
                this.colorQuantity = 0;
                return;
            }
        }

        String tempMiniTag;
        try {
            tempMiniTag = Formatter.parseMultiColor(args);
        } catch (IllegalArgumentException e) {
            this.primitive = "";
            this.value = "";
            this.colorQuantity = 0;
            return;
        }

        this.value = tempMiniTag;
        this.primitive = String.join(",", args);
        this.colorQuantity = args.length;
    }

    public MultiColor(@Nullable Team team) {
        this(team != null ? team.getStorage().getString(StoredTeamValue.MULTICOLOR) : null);
    }

    public @NotNull MultiColor(@Nullable String primitive) {
        this(primitive == null || primitive.isEmpty() ? null : primitive.split(","));
    }

    public String asCloseString() {
        switch (colorQuantity) {
            case 0:
                return "";
            case 1:
                return "</color>";
            default:
                return "</gradient>";
        }
    }

    public boolean isEmpty() {
        return primitive == null || primitive.isEmpty();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MultiColor
                && ((MultiColor) obj).primitive.equals(this.primitive)
                && ((MultiColor) obj).value.equals(this.value);
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }

    @Override
    public int compareTo(MultiColor o) {
        if (o == null) {
            return 1;
        }
        return this.primitive.compareTo(o.primitive);
    }
}
