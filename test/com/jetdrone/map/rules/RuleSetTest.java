package com.jetdrone.map.rules;

import org.junit.Test;

import static org.junit.Assert.fail;

public class RuleSetTest {

	@Test
	public void testRuleSetInputStream() {
		try {
			new RuleSet("osm-data/rule-test.xml");
		} catch (Exception e) {
			fail("rule set is wellformed");
		}
	}
}
