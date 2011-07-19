package com.jetdrone.map.source;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("serial")
class Relation implements Serializable {
	int id;
	Map<String, String> tags;
	Member member;
}
