package com.states.movement;

import com.Main;
import com.components.AnimationComponent;

public class RunningState extends MovementState {

	private float animationDuration = 0.6f;

	public RunningState(MovementContext context, MovementStateMachine.movementStates stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {
		AnimationComponent a = context.getEntity().getComponent(AnimationComponent.class);
		if(a != null) {
			a.setCurrentAnimation("Running");
			a.setAnimationDuration(animationDuration);
		}		
		nextState = stateKey;
	}

	@Override
	public void exit() {
	}

	@Override
	public void update(float dt) {
		velocity.x = 0;
		velocity.y = 0;
		int direction = 0;
		
		float speed = context.getEntity().getSpeed();
		if(context.getEntity().getActions("Up")){
			velocity.y = speed * dt;
		}else if(context.getEntity().getActions("Down")) {
			velocity.y = -speed * dt;
		}
		
		if(context.getEntity().getActions("Left")){
			direction = 1;
			velocity.x = -speed * dt;
		}else if(context.getEntity().getActions("Right")) {
			direction = -1;
			velocity.x = speed * dt;
		}
		
		if(context.getEntity().getActions("Roll")){
			nextState = MovementStateMachine.movementStates.Rolling;
			return;
		}
		
		if(velocity.x == 0 && velocity.y == 0) {
			nextState = MovementStateMachine.movementStates.Idle;
			return;
		}
		
		checkCollision();
		
		context.getEntity().transform.position.x += velocity.x;
		context.getEntity().transform.position.y += velocity.y;
		if(direction != 0) context.getEntity().transform.scale.x = Math.abs(context.getEntity().transform.scale.x) * direction;
		
		context.getEntity().setDirty(true);
		Main.getScene().updateGrid(context.getEntity());
	}

	@Override
	public MovementStateMachine.movementStates next() {
		return this.nextState;
	}

}
