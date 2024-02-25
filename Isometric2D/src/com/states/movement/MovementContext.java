package com.states.movement;

import com.components.AnimationComponent;
import com.entities.Entity;

public class MovementContext{
	private Entity entity;
	private AnimationComponent animation;
	
	public MovementContext(Entity entity) {
		this.entity = entity;
		this.animation = entity.getComponent(AnimationComponent.class);
	}

	public Entity getEntity() {
		return entity;
	}
	
	public AnimationComponent getAnimation() {
		return animation;
	}
}
