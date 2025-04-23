package com.booksaw.betterTeams;

import com.booksaw.betterTeams.message.Formatter;
import com.booksaw.betterTeams.message.Message;
import com.booksaw.betterTeams.message.MessageManager;
import com.booksaw.betterTeams.message.ReferenceMessage;
import com.booksaw.betterTeams.message.ReferencedFormatMessage;
import com.booksaw.betterTeams.message.StaticMessage;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

/**
 * This class is used to the the response for a command, this tracks if the
 * command was successful and what the message being sent to the user should be
 *
 * @author booksaw
 * @since 3.1.0
 */
public class CommandResponse {

	/**
	 * Used to track if the command was a success
	 */
	final boolean success;

	/**
	 * Used to track the message reference that should be sent to the user
	 */
	final Message message;

	/**
	 * Used for a basic successful command
	 *
	 * @param success if the command was successful
	 * @param message the reference for the message to send to the user
	 */
	public CommandResponse(boolean success, String reference) {
		this.success = success;
		this.message = new ReferenceMessage(reference);
	}

	public CommandResponse(boolean success, String reference, Object... replacement) {
		this.success = success;
		this.message = new ReferencedFormatMessage(reference, replacement);
	}

	public CommandResponse(boolean success, OfflinePlayer player, String reference) {
		this(success, new StaticMessage(Formatter.setPlaceholders(MessageManager.getMessage(reference), player)));
	}

	public CommandResponse(boolean success, OfflinePlayer player, String reference, Object... replacement) {
		this(success, new StaticMessage(Formatter.setPlaceholders(MessageManager.getMessage(reference, replacement), player)));
	}

	/**
	 * This method assumes that the command is not a success
	 *
	 * @param message the message to send to the user
	 */
	public CommandResponse(String reference) {
		this(false, reference);
	}

	public CommandResponse(String reference, Object... replacement) {
		this(false, reference, replacement);
	}

	public CommandResponse(OfflinePlayer player, String reference) {
		this(false, player, reference);
	}

	public CommandResponse(OfflinePlayer player, String reference, Object... replacement) {
		this(false, player, reference, replacement);
	}

	/**
	 * Used when you want to send a more complex message to the user
	 *
	 * @param success if the command was successful
	 * @param message the message to send to the user
	 */
	public CommandResponse(boolean success, Message message) {
		this.message = message;
		this.success = success;
	}

	/**
	 * Used when you want to send a more complex message to the user
	 *
	 * @param message the message to send to the user
	 */
	public CommandResponse(Message message) {
		this(false, message);
	}

	/**
	 * Used when no message should be sent to the user
	 *
	 * @param success if the command was a success or not
	 */
	public CommandResponse(boolean success) {
		message = null;
		this.success = success;
	}

	/**
	 * This method is used to send the message which has been set by the command
	 *
	 * @param sender the player who sent the command
	 */
	public void sendResponseMessage(CommandSender sender) {

		if (message == null) {
			return;
		}

		message.sendMessage(sender);
	}

	/**
	 * @return if the command was successful
	 */
	public boolean wasSuccessful() {
		return success;
	}

}
