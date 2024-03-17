package com.entities;

import java.util.List;

import org.joml.Vector2f;

import com.GameObject;
import com.Main;
import com.components.AABBComponent;
import com.utils.Transform;

public abstract class Projectile extends GameObject {
	protected Entity origin;
	private Vector2f trajectory;
	private float speed;
	
	public Projectile(String name, Transform transform, Vector2f trajectory, float speed, Entity origin) {
		super(name, transform);
		this.trajectory = trajectory;
		this.speed = speed;
		this.origin = origin;
		if(trajectory != null) {
			addComponents();
		}
	}
	
	public abstract void addComponents();
		
	@Override
	public void update(float dt) {
		updateComponents(dt);
		
		transform.position.x += trajectory.x * speed * dt;
		transform.position.y += trajectory.y * speed * dt;
		
		Main.getScene().updateGrid(this);
		
		checkCollision();
		
		setDirty(true);
	}
	
	public void checkCollision() {
		AABBComponent aabb = getComponent(AABBComponent.class);
		if(aabb != null) {
			List<GameObject> surrounding = Main.getScene().getSurroundingGrid(this, 2, 2);
			for(GameObject i : surrounding) {
				if(i == this) continue;
				if(i == origin) continue;
				if(aabb.getCollision(i.getComponent(AABBComponent.class), Entity.class)) {
					onCollision(i);
					return;
				}
			}
			surrounding = Main.getScene().getSurroundingLevel(this, 2, 2);
			for(GameObject i : surrounding) {
				if(aabb.getCollision(i.getComponent(AABBComponent.class))) {
					onCollision(i);
					return;
				}
			}
		}
	}
	
	public abstract void onCollision(GameObject o);
	
	public abstract Projectile create(String name, Vector2f position, Vector2f scale, Vector2f trajectory, float speed, Entity origin);

	public Vector2f getTrajectory() {
		return trajectory;
	}
	
	public void setTrajectory(Vector2f trajectory) {
		this.trajectory = trajectory;
	}
}
