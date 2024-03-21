package com.components.effects;

import com.entities.Entity;

public class SlowEffect extends StatusEffect {

	public SlowEffect(Entity target, float duration, float strength) {
		super(target, duration, strength);
	}

	@Override
	public void start() {
		strength = 1 / strength;
		if(target.getSpeedModifier() >= strength) {
			target.setSpeedModifier(strength);
		}
	}
	
	@Override
	public void exit() {
		if(target.getSpeedModifier() == strength) {
			target.setSpeedModifier(1);
		}		
	}

	@Override
	public void update(float dt) {		
	}

	@Override
	public StatusEffect create(Entity target, float duration, float strength) {
		return new SlowEffect(target, duration, strength);
	}

}
