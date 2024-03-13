package com.states.attack;

import com.components.AnimationComponent;
import com.entities.Entity;
import com.states.Context;

public class AttackContext extends Context<Entity>{
	private AnimationComponent animation;
	
	public AttackContext(Entity target) {
		super(target);
		this.animation = target.getComponent(AnimationComponent.class);
	}
	
	public AnimationComponent getAnimation() {
		return animation;
	}
}
