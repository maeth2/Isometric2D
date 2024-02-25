package com.entities;

import java.util.HashMap;
import java.util.Map;

import com.entities.players.Brynn;
import com.entities.players.Jaidyn;

public class EntityList {
	public static final Map<String, Entity> entities = new HashMap<String, Entity>();
	static {
		entities.put("Jaidyn", new Jaidyn("Jaidyn", null));
		entities.put("Brynn", new Brynn("Brynn", null));
	}
	public static Entity get(String name) {
		return entities.get(name);
	}
}