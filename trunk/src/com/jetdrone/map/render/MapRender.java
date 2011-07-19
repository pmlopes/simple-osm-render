package com.jetdrone.map.render;

import static com.jetdrone.map.render.Options.MAP;
import static com.jetdrone.map.render.Options.TILE;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jetdrone.map.render.backend.Renderer;
import com.jetdrone.map.rules.RuleSet;
import com.jetdrone.map.source.MapSource;

public class MapRender {

	private static final int RESOLUTION = 256;

	// Start a Thread pool to share the load of this rendering task
	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	@SuppressWarnings("boxing")
	private static void drawFullMap(Renderer renderer) throws IOException {
		// Initialize all layers
		for (int z = Options.minlayer; z <= Options.maxlayer; z++) {
			// Save Images
			String filename = String.format("%s/%02d.png", Options.outdir, z);
			renderer.drawMap(filename, z);
		}
	}

	private static void drawTileMap(Renderer renderer, int zoom_level) throws IOException {
		int maxx = renderer.getMaxXTile(zoom_level);
		int maxy = renderer.getMaxYTile(zoom_level);

		for (int i = renderer.getMinXTile(zoom_level); i <= maxx; i++) {
			for (int j = renderer.getMinYTile(zoom_level); j <= maxy; j++) {
				EXECUTOR.execute(new RenderTask(renderer, i, j, zoom_level));
//				renderer.drawTile(String.format("tiles/%d_%d.png", i, j), i, j, zoom_level);
			}
		}
	}

	public static void main(String[] args) throws Exception {

//		new java.io.BufferedReader(new java.io.InputStreamReader(System.in)).readLine();

		if (!Options.parse(args)) {
			System.out.println("Usage: render -{m|t} [--zoom] [--minlayer] [--maxlayer] [-o DIR] <RULESFILE> <OSMFILE>");
			System.exit(1);
		}

		RuleSet ruleset = new RuleSet(Options.cfgfn);
		MapSource map = new MapSource(Options.osmfn);
		
		File outdir = new File(Options.outdir);
		if (!outdir.exists())
			System.out.println("Create missing directories: " + outdir.mkdirs());
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			final long t0 = System.nanoTime();
			
			public void run() {
				System.out.println("Total render time: " + (System.nanoTime() - t0) + "ns");
			}
		});
		
		if (Options.mode == MAP) {
			drawFullMap(new Renderer(ruleset, map));
		} else if (Options.mode == TILE) {
			drawTileMap(new Renderer(ruleset, map, RESOLUTION), Options.zoom_level);
		} else {
			System.out.println("Usage: render -{m|t} [--zoom] [--minlayer] [--maxlayer] [-o DIR] <RULESFILE> <OSMFILE>");
			System.exit(1);
		}

		EXECUTOR.shutdown();
	}
}
