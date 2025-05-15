package com.booksaw.betterTeams.exceptions;

import org.bukkit.ChatColor;

public class InvalidChatColorException extends InvalidArgumentException {

    private static final long serialVersionUID = 7281044554751226962L;
	
	public InvalidChatColorException(String message, ChatColor invalid) {
		super(message, invalid);
	}

	public InvalidChatColorException(String message, ChatColor invalid, Throwable cause) {
		super(message, invalid, cause);
	}

	public InvalidChatColorException(ChatColor invalid, Throwable cause) {
		super(invalid, cause);
	}
}
