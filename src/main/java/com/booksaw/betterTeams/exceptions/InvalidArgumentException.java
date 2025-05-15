package com.booksaw.betterTeams.exceptions;

import lombok.Getter;

public class InvalidArgumentException extends IllegalArgumentException {

	private static final long serialVersionUID = 1850117442578766225L;

	/**
	 * The invalid argument that was passed in
	 */
	@Getter
	private final Object invalid;

	public InvalidArgumentException(String message, Object invalid) {
		super(message);
		this.invalid = invalid;
	}

	public InvalidArgumentException(String message, Object invalid, Throwable cause) {
		super(message, cause);
		this.invalid = invalid;
	}

	public InvalidArgumentException(Object invalid, Throwable cause) {
		super(cause);
		this.invalid = invalid;
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

}
