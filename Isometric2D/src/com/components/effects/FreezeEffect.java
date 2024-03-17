package com.components.effects;

import com.Main;
import com.components.AnimationComponent;
import com.components.TextureComponent;
import com.entities.Entity;

public class FreezeEffect extends StatusEffect {

	private boolean isHit = false;
	
	public FreezeEffect(Entity target, float duration, float strength) {
		super(target, duration, strength);
	}

	@Override
	public void start() {
		target.getComponent(AnimationComponent.class).setFreeze(true);
		target.setSpeedModifier(0f);
		isHit = true;
	}
	
	@Override
	public void exit() {
		target.getComponent(AnimationComponent.class).setFreeze(false);		
		target.getComponent(TextureComponent.class).setSpriteColor(-1, -1, -1);
		target.setSpeedModifier(1f);
	}

	@Override
	public void update(float dt) {
		target.getComponent(TextureComponent.class).setSpriteColor(0, 0, 1);
		if(!isHit && target.getTrigger("Damaged")) {
			isFinished = true;
		}
		isHit = false;
	}
	
	@Override
	public void stack(float duration) {
		this.duration = Main.getTimeElapsed() - this.startTime + duration;
		this.startTime = Main.getTimeElapsed();
		isHit = true;
	}

	@Override
	public StatusEffect create(Entity target, float duration, float strength) {
		return new FreezeEffect(target, duration, strength);
	}
}
