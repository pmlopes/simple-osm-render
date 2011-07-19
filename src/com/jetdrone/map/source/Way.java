package com.jetdrone.map.source;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jetdrone.map.BoundingBox;

@SuppressWarnings("serial")
public class Way extends BoundingBox implements Serializable {

	private final List<Node> nd;

	private int layer;
	private String name;
	private Map<String, String> tags;

	public Way() {
		super(360.0, 360.0, -360.0, -360.0);
		nd = new ArrayList<Node>();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
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

	public void addWayNode(Node node) {
		nd.add(node);
		double lat = node.getLat();
		double lon = node.getLon();
		
		if(lat < getMinLat()) setMinLat(lat);
		if(lat > getMaxLat()) setMaxLat(lat);
		if(lon < getMinLon()) setMinLon(lon);
		if(lon > getMaxLon()) setMaxLon(lon);
	}

	public Map<String, String> getTags() {
		return tags;
	}

	public List<Node> getWayNode() {
		return nd;
	}
}
