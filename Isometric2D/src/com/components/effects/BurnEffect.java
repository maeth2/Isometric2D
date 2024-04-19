package com.components.effects;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.Main;
import com.components.TextureComponent;
import com.entities.Entity;
import com.scenes.Scene;

public class BurnEffect extends StatusEffect {

	private float tickDuration = 0.5f;
	private float tickElapsed = 0f;
	
	private float flickerDuration = 0.15f;
	private float flickerElapsed = 0f;
	
	public BurnEffect(Entity target, float duration, float strength) {
		super(target, duration, strength);
	}

	@Override
	public void start() {
		renderParticles();
	}
	
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
			target.onDamage(50);
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
	
	@Override
	protected void renderParticles() {
		Main.getScene().getParticles().add(
			"BURNING", 
			new Vector2f(
				target.getTransform().getPosition().x + Main.random.nextFloat() * Scene.UNIT_SIZE / 4f, 
				target.getTransform().getPosition().y + Main.random.nextFloat() * Scene.UNIT_SIZE / 4f
			),
			15f, 
			new Vector2f(0, 30f),
			0.75f,
			new Vector3f(1, 0, 0)
		);
	}
}
