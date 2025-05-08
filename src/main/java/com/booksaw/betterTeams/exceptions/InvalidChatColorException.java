package com.booksaw.betterTeams.exceptions;

import org.bukkit.ChatColor;

import lombok.Getter;

public class InvalidChatColorException extends IllegalArgumentException {

    private static final long serialVersionUID = 7281044554751226962L;

	/**
	 * The invalid chat color that was passed in
	 */
	@Getter
	private final ChatColor invalid;

	public InvalidChatColorException(String message, ChatColor invalid) {
		super(message);
		this.invalid = invalid;
	}

	public InvalidChatColorException(String message, ChatColor invalid, Throwable cause) {
		super(message, cause);
		this.invalid = invalid;
	}

	public InvalidChatColorException(ChatColor invalid, Throwable cause) {
		super(cause);
		this.invalid = invalid;
	}

}
