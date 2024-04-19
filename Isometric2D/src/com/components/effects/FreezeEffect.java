package com.components.effects;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.Main;
import com.components.AnimationComponent;
import com.components.TextureComponent;
import com.entities.Entity;
import com.scenes.Scene;

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
		renderParticles();
	}
	
	@Override
	public void exit() {
		target.getComponent(AnimationComponent.class).setFreeze(false);		
		target.getComponent(TextureComponent.class).setColor(-1, -1, -1);
		target.setSpeedModifier(1f);
	}

	@Override
	public void update(float dt) {
		target.getComponent(TextureComponent.class).setColor(0, 0, 1);
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
		renderParticles();
	}

	@Override
	public StatusEffect create(Entity target, float duration, float strength) {
		return new FreezeEffect(target, duration, strength);
	}
	
	@Override
	protected void renderParticles() {
		Main.getScene().getParticles().add(
			"FROZEN", 
			new Vector2f(
				target.getTransform().getPosition().x + Main.random.nextFloat() * Scene.UNIT_SIZE / 4f, 
				target.getTransform().getPosition().y + Main.random.nextFloat() * Scene.UNIT_SIZE / 4f
			),
			15f, 
			new Vector2f(0, 50f),
			0.5f,
			new Vector3f(0, 206, 209)
		);
	}
}
