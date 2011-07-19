package com.jetdrone.map.render.backend;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_COLOR_RENDERING;
import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.KEY_RENDERING;
import static java.awt.RenderingHints.KEY_STROKE_CONTROL;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC;
import static java.awt.RenderingHints.VALUE_RENDER_QUALITY;
import static java.awt.RenderingHints.VALUE_STROKE_NORMALIZE;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_GASP;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.jetdrone.map.BoundingBox;
import com.jetdrone.map.Coordinate;
import com.objectplanet.image.PngEncoder;

public class MapSurface {

	private static final Map<RenderingHints.Key, Object> RENDER_HINTS = new HashMap<RenderingHints.Key, Object>();

	private static final boolean HEADLESS;
	private static final GraphicsConfiguration GC;

	private static final PngEncoder ENCODER = new PngEncoder(PngEncoder.COLOR_TRUECOLOR, PngEncoder.BEST_COMPRESSION);

	private final Image surface;
	private final Graphics2D graphics;

	// image metadata (immutable)
	public final Coordinate offset;
	public final BoundingBox bounds;
	
	public final int zoomLevel;
	public final GeneralPath path;

	static {
		if (GraphicsEnvironment.isHeadless()) {
			HEADLESS = true;
			GC = null;
		} else {
			HEADLESS = false;
			GC = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		}

		RENDER_HINTS.put(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
		RENDER_HINTS.put(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_QUALITY);
		RENDER_HINTS.put(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC);
		RENDER_HINTS.put(KEY_RENDERING, VALUE_RENDER_QUALITY);
		RENDER_HINTS.put(KEY_STROKE_CONTROL, VALUE_STROKE_NORMALIZE);
		RENDER_HINTS.put(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_GASP);
	}

	public MapSurface(int width, int height, Color bg, int zoom_level, Coordinate offset, BoundingBox bounds) {
		System.out.println(width + "x" + height);
		if (HEADLESS) {
			surface = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		} else {
			if (width == 256 && height == 256) {
				surface = GC.createCompatibleVolatileImage(256, 256);
				surface.setAccelerationPriority(1);
			} else {
				surface = GC.createCompatibleImage(width, height);
				surface.setAccelerationPriority(1);
			}
		}
		graphics = (Graphics2D) surface.getGraphics();
		path = new GeneralPath();

		this.zoomLevel = zoom_level;
		this.offset = offset;
		this.bounds = bounds;
		
		graphics.setBackground(bg);
		graphics.clearRect(0, 0, width, height);
		
		graphics.setRenderingHints(RENDER_HINTS);
	}

	public Graphics2D getGraphics() {
		return graphics;
	}

	public void write(final String filename) throws IOException {
		ENCODER.encode(surface, new FileOutputStream(filename));
	}

	public void write(final OutputStream out) throws IOException {
		ENCODER.encode(surface, out);
	}
	
	public void flush() {
		surface.flush();
	}
}
