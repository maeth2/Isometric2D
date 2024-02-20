package com.entities;

import java.util.HashMap;
import java.util.Map;

public class EntityList {
	public static final Map<String, Entity> entities = new HashMap<String, Entity>();
	static {
		entities.put("Jaidyn", new Jaidyn(null));
	}
	public static Entity get(String name) {
		return entities.get(name);
	}
}
