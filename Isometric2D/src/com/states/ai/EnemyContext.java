package com.states.ai;

import org.joml.Vector2f;

import com.entities.Entity;
import com.scenes.Scene;
import com.states.Context;
import com.states.movement.MovementStateMachine;

public class EnemyContext extends Context<Entity> {
	protected Vector2f destination;
	protected MovementStateMachine movement;
	protected Entity targetEntity;
	protected float chaseRadius = Scene.UNIT_SIZE * 0.5f;
	protected float attackRadius = Scene.UNIT_SIZE * 0.5f;
	
	public EnemyContext(Entity target) {
		super(target);
		destination = new Vector2f(-1, -1);
		movement = target.getStateMachine(MovementStateMachine.class);
	}
	
	public void setDestination(float x, float y) {
		this.destination.x = x;
		this.destination.y = y;
	}
	
	public void setTargetEntity(Entity target) {
		this.targetEntity = target;
	}
	
	public Entity getTargetEntity() {
		return this.targetEntity;
	}
}
