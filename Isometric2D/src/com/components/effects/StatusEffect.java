package com.components.effects;

import java.util.HashMap;
import java.util.Map;

import com.Main;
import com.entities.Entity;

public abstract class StatusEffect {
	public static enum effects{
		Slow,
		Poision,
		Freeze,
		Burn
	}
	
	public static Map<effects, StatusEffect> statusEffects = new HashMap<effects, StatusEffect>();
	static {
		statusEffects.put(effects.Slow, new SlowEffect(null, 0, 0));
		statusEffects.put(effects.Poision, new PoisonEffect(null, 0, 0));
		statusEffects.put(effects.Freeze, new FreezeEffect(null, 0, 0));
		statusEffects.put(effects.Burn, new BurnEffect(null, 0, 0));
	}
	
	protected Entity target;
	protected float duration;
	protected float strength;
	protected float startTime;
	protected boolean isFinished = false;
	
	public StatusEffect(Entity target, float duration, float strength) {
		this.target = target;
		this.duration = duration;
		this.strength = strength;
		this.startTime = Main.getTimeElapsed();
	}
	
	public abstract void start();
	
	public abstract void exit();
	
	public abstract void update(float dt);
		
	public abstract StatusEffect create(Entity target, float duration, float strength);
	
	public boolean checkDuration() {
		return startTime + duration < Main.getTimeElapsed();
	}
	
	public boolean getFinished() {
		return this.isFinished;
	}
	
	public void stack(float duration) {
		this.startTime = Main.getTimeElapsed();
		this.duration = duration;
		renderParticles();
	}
	
	protected void renderParticles() {}
}
