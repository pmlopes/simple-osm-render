package com.jetdrone.map.source;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class Node implements Serializable {

	private double lat;
	private double lon;
	private int layer;

	private Map<String, String> tags;

	public Node(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public void insertTag(String key, String value) {
		
		if(tags == null) tags = new HashMap<String, String>();
		tags.put(key, value);
	}
}
