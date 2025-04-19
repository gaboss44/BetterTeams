package com.booksaw.betterTeams.color;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.message.Formatter;
import com.booksaw.betterTeams.team.storage.team.StoredTeamValue;

import lombok.Getter;

/**
 * A class which represents a multicolor for a {@link Team}
 * <p>
 * This class is used to store the primitive string value of the multicolor
 * and the mini tag which represents the multicolor in the chat.
 */
public class MultiColor implements Comparable<MultiColor>,Serializable,CharSequence {

    /**
     * The primitive string which represent this multicolor's raw value
     */
    @Getter
    private String primitive = "";

    /**
     * The mini tag which represent this multicolor
     */
    @Getter
    private String miniTag = "";

    public MultiColor() {
    }

    public MultiColor(@Nullable String... args) {
        if (args == null || args.length == 0) {
            return;
        }

        try {
            this.miniTag = Formatter.parseMulticolor(args);
        } catch (IllegalArgumentException e) {
            return;
        }

        this.primitive = String.join("", args);
    }

    public MultiColor(Team team) {
        this(team != null ? team.getStorage().getString(StoredTeamValue.MULTICOLOR) : null);
    }

    public @NotNull MultiColor(@Nullable String primitive) {
        this(primitive == null || primitive.isEmpty() ? null : primitive.split(","));
    }

    public boolean isEmpty() {
        return primitive == null || primitive.isEmpty();
    }

    @Override
    public String toString() {
        return miniTag;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MultiColor
                && ((MultiColor) obj).getPrimitive().equals(this.primitive)
                && ((MultiColor) obj).getMiniTag().equals(this.miniTag);
    }

    @Override
    public char charAt(int index) {
        return miniTag.charAt(index);
    }

    @Override
    public int length() {
        return miniTag.length();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return miniTag.subSequence(start, end);
    }

    @Override
    public int compareTo(MultiColor o) {
        if (o == null) {
            return 1;
        }
        return this.primitive.compareTo(o.getPrimitive());
    }
}
