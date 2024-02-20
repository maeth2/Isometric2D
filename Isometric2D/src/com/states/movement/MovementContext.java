package com.states.movement;

import com.entities.Entity;

public class MovementContext{
	private Entity entity;
	
	public MovementContext(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
}
