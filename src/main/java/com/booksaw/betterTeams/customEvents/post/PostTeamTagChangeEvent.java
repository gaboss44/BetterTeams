package com.booksaw.betterTeams.customEvents.post;

import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.customEvents.TeamEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * An event which is called right before the tag of a {@link Team} is changed
 */
public class PostTeamTagChangeEvent extends TeamEvent {
    private final String oldTag;
    private final String newTag;

    public PostTeamTagChangeEvent(@NotNull Team team,
                                  @NotNull String oldTag,
                                  @NotNull String newTag) {
        super(team, true);
        this.oldTag = oldTag;
        this.newTag = newTag;
    }

    public String getOldTeamTag() {
        return oldTag;
    }

    public String getNewTeamTag() {
        return newTag;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
