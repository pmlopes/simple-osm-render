package com.jetdrone.map.render;

import java.io.IOException;

import com.jetdrone.map.render.backend.Renderer;

public class RenderTask implements Runnable {

	private final Renderer renderer;
	private final String filename;
	private final int x, y, zoom_level;

	@SuppressWarnings("boxing")
	public RenderTask(Renderer renderer, int x, int y, int zoom_level) {
		this.renderer = renderer;
		this.filename = String.format("tiles/%d_%d.png", x, y);
		this.x = x;
		this.y = y;
		this.zoom_level = zoom_level;
	}

	@Override
	public void run() {
		try {
			renderer.drawTile(filename, x, y, zoom_level);
		} catch (IOException e) {
			System.err.println("Error: RenderTask[" + this + "] caused by [" + e + "]");
		}
	}
}
