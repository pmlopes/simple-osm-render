package com.jetdrone.map.render;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jetdrone.map.render.backend.Renderer;
import com.jetdrone.map.rules.RuleSet;
import com.jetdrone.map.source.MapSource;

@SuppressWarnings("serial")
public class MapRenderServlet extends HttpServlet {
	
	private static final Renderer renderer;

	static {
		RuleSet ruleset = null;
		MapSource map = null;
		Renderer instance = null;
		try {
			ruleset = new RuleSet(MapRenderServlet.class.getResourceAsStream("rule.xml"));
			map = new MapSource(MapRenderServlet.class.getResourceAsStream("netherlands.osm.idx"));
			instance = new Renderer(ruleset, map);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderer = instance;
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String[] args = request.getQueryString().split("/");
		int zoom_level = Integer.parseInt(args[0]);
		int x = Integer.parseInt(args[1]);
		int y = Integer.parseInt(args[2]);
		response.setContentType("image/png");
		renderer.drawTile(response.getOutputStream(), x, y, zoom_level);
	}
}
