package com.components.effects;

import com.components.TextureComponent;
import com.entities.Entity;

public class BurnEffect extends StatusEffect {

	private float tickDuration = 0.5f;
	private float tickElapsed = 0f;
	
	private float flickerDuration = 0.15f;
	private float flickerElapsed = 0f;
	
	public BurnEffect(Entity target, float duration, float strength) {
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
			target.getComponent(TextureComponent.class).setColor(1, 0, 0);
			flickerElapsed = 0f;
			tickElapsed = 0;
		}
		if(flickerElapsed > flickerDuration) {
			target.getComponent(TextureComponent.class).setColor(-1, -1, -1);
		}		
	}

	@Override
	public StatusEffect create(Entity target, float duration, float strength) {
		return new BurnEffect(target, duration, strength);
	}

}
