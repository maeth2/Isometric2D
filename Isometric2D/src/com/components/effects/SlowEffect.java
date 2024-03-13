package com.components.effects;

import com.entities.Entity;

public class SlowEffect extends StatusEffect {

	public SlowEffect(Entity target, float duration, float strength) {
		super(target, duration, strength);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float dt) {		
	}

	@Override
	public StatusEffect create(Entity target, float duration, float strength) {
		return new SlowEffect(target, duration, strength);
	}

}
