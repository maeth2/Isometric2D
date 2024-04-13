package com.components.effects;

import com.components.TextureComponent;
import com.entities.Entity;

public class PoisonEffect extends StatusEffect {

	private float tickDuration = 1f;
	private float tickElapsed = 0f;
	
	private float flickerDuration = 0.25f;
	private float flickerElapsed = 0f;
	
	public PoisonEffect(Entity target, float duration, float strength) {
		super(target, duration, strength);
	}

	@Override
	public void start() {}
	
	@Override
	public void exit() {
		target.getComponent(TextureComponent.class).setColor(-1, -1, -1);
	}
	
	@Override
	public void update(float dt) {
		tickElapsed += dt;
		flickerElapsed += dt;
		if(tickElapsed >= tickDuration) {
			target.getComponent(TextureComponent.class).setColor(0, 1, 0);
			flickerElapsed = 0f;
			tickElapsed = 0;
		}
		if(flickerElapsed > flickerDuration) {
			target.getComponent(TextureComponent.class).setColor(-1, -1, -1);
		}
	}

	@Override
	public StatusEffect create(Entity target, float duration, float strength) {
		return new PoisonEffect(target, duration, strength);
	}

}
