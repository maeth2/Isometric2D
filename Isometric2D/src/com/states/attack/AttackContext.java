package com.states.attack;

import com.entities.Entity;

public class AttackContext{
	private Entity entity;
	
	public AttackContext(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
}
