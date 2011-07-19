package com.jetdrone.map.source;

import java.io.Serializable;

@SuppressWarnings("serial")
class Member implements Serializable {
	Node node;
	Way way;
	String role;
	Member next;
}
