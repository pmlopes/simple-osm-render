package com.jetdrone.map.source.osm;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.jetdrone.map.BoundingBox;
import com.jetdrone.map.MapException;
import com.jetdrone.map.source.Node;
import com.jetdrone.map.source.Way;

public abstract class OSMReader extends DefaultHandler {
	
	Map<Integer, Node> nodeidx = new HashMap<Integer, Node>(); // Node Hash

	private Node cNode = null;
	private Way cWay = null;
	
	private static final Logger LOG = Logger.getLogger(OSMReader.class.getName());
	
	public void load(InputStream stream) throws MapException {
		try {
			// Create a builder factory
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(false);
			// Create the builder and parse the file
			factory.newSAXParser().parse(stream, this);
		
			nodeidx.clear();
			nodeidx = null;
		} catch(SAXException e) {
			throw new MapException(e);
		} catch(IOException e) {
			throw new MapException(e);
		} catch(ParserConfigurationException e) {
			throw new MapException(e);
		}
	}
	
	@Override
	@SuppressWarnings("boxing")
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//		LOG.fine("start element " + qName);
		// Parsing Bounds
		if ("bounds".equals(qName)) {
//			LOG.fine("Parsing bounds");
			initIndex(new BoundingBox(
					Double.parseDouble(attributes.getValue("minlat")),
					Double.parseDouble(attributes.getValue("minlon")),
					Double.parseDouble(attributes.getValue("maxlat")),
					Double.parseDouble(attributes.getValue("maxlon"))));
		}
		// Parsing bound (OSM 0.6) using osmosis
		if ("bound".equals(qName)) {
//			LOG.fine("Parsing bound");
			String[] box = attributes.getValue("box").split(",");
			initIndex(new BoundingBox(
					Double.parseDouble(box[0]),
					Double.parseDouble(box[1]),
					Double.parseDouble(box[2]),
					Double.parseDouble(box[3])));
		}
		// Parsing Node
		else if ("node".equals(qName)) {
//			LOG.fine("Parsing Node");
			int id = Integer.parseInt(attributes.getValue("id"));
			cNode = new Node(
					Double.parseDouble(attributes.getValue("lat")),
					Double.parseDouble(attributes.getValue("lon")));

			// Insert Node local hash
			nodeidx.put(id, cNode);
		}
		// Parsing Tags
		else if ("tag".equals(qName)) {
//			LOG.fine("Parsing Tag");

			if (cNode == null && cWay == null) // End if there is nothing to add
												// the tag to
				return;

			String k, v;

			k = attributes.getValue("k").intern();
			v = attributes.getValue("v").intern();
			// attributes.getValue("created_by");
			// attributes.getValue("source");
			if ("layer".equals(k)) {
				int layer;
				try {
					if(v.charAt(0) == '+') v = v.substring(1);
					layer = Integer.parseInt(v);
				} catch(NumberFormatException nfe) {
					LOG.severe("Not a number: " + v);
					layer = 1;
				}
				if (cNode != null) {
					cNode.setLayer(layer);
				} else {
					cWay.setLayer(layer);
				}
			} else if ("name".equals(k)) {
				if (cWay != null) {
					cWay.setName(v);
				}
			}

			if (cNode != null)
				cNode.insertTag(k, v);
			else if (cWay != null)
				cWay.insertTag(k, v);
		}
		// Parsing Way
		else if ("way".equals(qName)) {
//			LOG.fine("Parsing Way");
			cWay = new Way();
		}
		// Parsing WayNode
		else if ("nd".equals(qName)) {
//			LOG.fine("Parsing Nd");
			int ref = Integer.parseInt(attributes.getValue("ref"));

			if (ref != 0) {
				Node n;

				n = nodeidx.get(ref);
				if (n == null) {
					LOG.severe("No node with reference " + ref + " found!");
					return;
				}

				// Insert WayNode
				cWay.addWayNode(n);
				cNode = null;
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
//		LOG.fine("end element");
		if ("node".equals(qName)) {
			if(cNode != null) {
				indexNode(cNode);
			}
			cNode = null;
		} else if ("way".equals(qName)) {
			if(cWay != null) {
				indexWay(cWay);
			}
			cWay = null;
		}
	}
	
	public abstract void initIndex(BoundingBox bbox);
	
	public abstract void indexWay(Way w);
	
	public abstract void indexNode(Node n);
}
