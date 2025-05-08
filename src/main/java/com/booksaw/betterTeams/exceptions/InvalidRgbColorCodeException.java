package com.booksaw.betterTeams.exceptions;

import lombok.Getter;

public class InvalidRgbColorCodeException extends IllegalArgumentException {

	private static final long serialVersionUID = 9223372036854775807L;

	/**
	 * The invalid color code that was passed in
	 */
	@Getter
	private final String invalid;

	public InvalidRgbColorCodeException(String message, String invalid) {
		super(message);
		this.invalid = invalid;
	}

	public InvalidRgbColorCodeException(String message, String invalid, Throwable cause) {
		super(message, cause);
		this.invalid = invalid;
	}

	public InvalidRgbColorCodeException(String invalid, Throwable cause) {
		super(cause);
		this.invalid = invalid;
	}
}
