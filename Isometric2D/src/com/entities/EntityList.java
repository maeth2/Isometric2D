package com.entities;

import java.util.HashMap;
import java.util.Map;

import com.entities.players.Brynn;
import com.entities.players.Jaidyn;

public class EntityList {
	
	public static enum entity{
		Jaidyn,
		Brynn,
	}
	
	public static final Map<EntityList.entity, Entity> entities = new HashMap<EntityList.entity, Entity>();
	static {
		entities.put(entity.Jaidyn, new Jaidyn("Jaidyn", null));
		entities.put(entity.Brynn, new Brynn("Brynn", null));
	}
	
	public static Entity get(EntityList.entity e) {
		return entities.get(e);
	}
}