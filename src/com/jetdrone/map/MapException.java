package com.jetdrone.map;

@SuppressWarnings("serial")
public final class MapException extends Exception {

	public MapException(String message) {
		super(message);
	}

	public MapException(Throwable cause) {
		super(cause);
	}

	public MapException(String message, Throwable cause) {
		super(message, cause);
	}
}
