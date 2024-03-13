package com.components.effects;

import com.components.AnimationComponent;
import com.components.TextureComponent;
import com.entities.Entity;

public class FreezeEffect extends StatusEffect {

	public FreezeEffect(Entity target, float duration, float strength) {
		super(target, duration, strength);
	}

	@Override
	public void start() {
		target.getComponent(AnimationComponent.class).setFreeze(true);
	}
	
	@Override
	public void exit() {
		target.getComponent(AnimationComponent.class).setFreeze(false);		
		target.getComponent(TextureComponent.class).setSpriteColor(-1, -1, -1);
	}

	@Override
	public void update(float dt) {
		target.getComponent(TextureComponent.class).setSpriteColor(0, 0, 1);
	}

	@Override
	public StatusEffect create(Entity target, float duration, float strength) {
		return new FreezeEffect(target, duration, strength);
	}
}
