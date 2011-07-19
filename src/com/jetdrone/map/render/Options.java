package com.jetdrone.map.render;

public final class Options {
	
	public static final int MAP = 1;
	public static final int TILE = 2;

	/* render mode */
	static int mode = MAP;

	static int minlayer = 12;
	static int maxlayer = 17;
	static int zoom_level = 14;
	static String cfgfn;
	static String osmfn;
	static String outdir = "tiles"; /* tiles output directory */

	private Options() {
		// singleton
	}

	public static boolean parse(String[] args) {
		try {
			for (int i = 0; i < args.length; i++) {
				if ("-m".equals(args[i])) {
					mode = MAP;
					continue;
				}
				if ("-t".equals(args[i])) {
					mode = TILE;
					continue;
				}
				if ("--minlayer".equals(args[i])) {
					minlayer = Integer.parseInt(args[++i]);
					continue;
				}
				if ("--maxlayer".equals(args[i])) {
					maxlayer = Integer.parseInt(args[++i]);
					continue;
				}
				if ("--zoom".equals(args[i])) {
					zoom_level = Integer.parseInt(args[++i]);
					minlayer = zoom_level;
					maxlayer = zoom_level;
					continue;
				}
				if ("-o".equals(args[i])) {
					outdir = args[++i];
					continue;
				}
				cfgfn = args[i];
				osmfn = args[++i];
				break;
			}
		} catch (RuntimeException re) {
			return false;
		}

		return !(cfgfn == null || osmfn == null);
	}
}
