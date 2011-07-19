package com.jetdrone.map.source;

import org.junit.Test;

import static org.junit.Assert.fail;

public class MapSourceTest {

	@Test
	public void testParseOSMFile() {
		try {
			// new MapSource("osm-data/map-test.osm");
			new MapSource("osm-data/map-osm6.osm");
		} catch (Exception e) {
			fail("OSM file is wellformed");
		}
	}
}
