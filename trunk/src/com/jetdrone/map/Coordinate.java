package com.jetdrone.map;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Coordinate implements Serializable {

	public final double x;
	public final double y;

	public Coordinate(final double x, final double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "<" + x + ":" + y + ">";
	}
}
