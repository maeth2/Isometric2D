package com.states.movement;

import org.joml.Vector2f;

import com.components.AnimationComponent;
import com.entities.Entity;
import com.states.Context;

public class MovementContext extends Context<Entity>{
	private AnimationComponent animation;
	private Vector2f knockback = new Vector2f();
		
	public MovementContext(Entity target) {
		super(target);
		this.animation = target.getComponent(AnimationComponent.class);
	}
	
	public AnimationComponent getAnimation() {
		return animation;
	}
	
	public void setKnockbackDistance(float dx, float dy) {
		this.knockback.x = dx;
		this.knockback.y = dy;
	}
	
	public Vector2f getKnockback() {
		return this.knockback;
	}
}
