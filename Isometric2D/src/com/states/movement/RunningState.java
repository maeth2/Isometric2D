package com.states.movement;

import com.Main;
import com.components.AnimationComponent;

public class RunningState extends MovementState {

	public RunningState(MovementContext context, MovementStateMachine.movementStates stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {
		AnimationComponent a = context.getEntity().getComponent(AnimationComponent.class);
		if(a != null) {
			a.setCurrentAnimation("Running");
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
		
		float speed = context.getEntity().getSpeed();
		
		if(context.getEntity().getTrigger("Up")){
			velocity.y = speed * dt;
		}else if(context.getEntity().getTrigger("Down")) {
			velocity.y = -speed * dt;
		}
		
		if(context.getEntity().getTrigger("Left")){
			context.getEntity().transform.scale.x = Math.abs(context.getEntity().transform.scale.x);
			velocity.x = -speed * dt;
		}else if(context.getEntity().getTrigger("Right")) {
			context.getEntity().transform.scale.x = -Math.abs(context.getEntity().transform.scale.x);
			velocity.x = speed * dt;
		}
		
		if(context.getEntity().getTrigger("Roll")){
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
		
		Main.getScene().updateGrid(context.getEntity());
		context.getEntity().setDirty(true);
	}
}
