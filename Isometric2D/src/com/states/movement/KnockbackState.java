package com.states.movement;

import com.components.TextureComponent;
import com.entities.Entity;

public class KnockbackState extends MovementState {
	
	private float duration = 0.15f;
	private float elapsed = 0f;
	
	public KnockbackState(MovementContext context, MovementStateMachine.state stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {
		context.getAnimation().setCurrentAnimation(Entity.states.Idle);
		context.getTarget().getComponent(TextureComponent.class).setSpriteColor(1f, 1f, 1f);
		elapsed = 0f;
	}

	@Override
	public void exit() {	
		context.getTarget().getComponent(TextureComponent.class).setSpriteColor(-1f, -1f, -1f);
	}

	@Override
	public void update(float dt) {
		elapsed += dt;
		
		velocity.x = context.getKnockback().x / duration * dt;
		velocity.y = context.getKnockback().y / duration * dt;
		
		checkCollision();
		
		context.getTarget().changePosition(velocity.x, velocity.y);
		
		if((velocity.x == 0 && velocity.y == 0) || elapsed >= duration) {
			this.nextState = MovementStateMachine.state.Idle;
		}
	}
}
