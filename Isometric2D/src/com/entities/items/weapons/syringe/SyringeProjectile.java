package com.entities.items.weapons.syringe;

import org.joml.Vector2f;

import com.GameObject;
import com.components.AABBComponent;
import com.components.TextureComponent;
import com.components.effects.StatusEffect;
import com.entities.Entity;
import com.entities.Projectile;
import com.utils.AssetManager;
import com.utils.Texture;
import com.utils.Transform;

public class SyringeProjectile extends Projectile {

	public static Texture spriteSheet = AssetManager.getTexture("assets/textures/weapons.png");

	public SyringeProjectile(String name, Transform transform, Vector2f trajectory, float speed, Entity origin) {
		super(name, transform, trajectory, speed, origin);
	}

	@Override
	public void addComponents() {
		addComponent(new TextureComponent(spriteSheet, false, new Vector2f(4, 0), new Vector2f(16, 16)));	
		addComponent(new AABBComponent(this, new Vector2f(0, 0), transform.scale));
	}
	
	@Override
	public void onCollision(GameObject o) {
		if(o instanceof Entity) {
			Entity e = (Entity) o;
			e.onHit(origin, getTrajectory(), 10f, 10f);
			e.apply(StatusEffect.effects.Freeze, 4f, 1f);
		}
		kill();
	}

	@Override
	public Projectile create(String name,  Vector2f position, Vector2f scale, Vector2f trajectory, float speed, Entity origin) {
		return new SyringeProjectile(
				name, 
				new Transform(new Vector2f(position.x, position.y), new Vector2f(scale.x, scale.y)),
				trajectory,
				speed,
				origin
		);
	}
}
