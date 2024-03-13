package com.states.movement;

import com.entities.Entity;

public class RunningState extends MovementState {
	public RunningState(MovementContext context, MovementStateMachine.state stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {
		if(context.getAnimation() != null) {
			context.getAnimation().setCurrentAnimation(Entity.states.Running);
		}		
	}

	@Override
	public void exit() {
	}

	@Override
	public void update(float dt) {
		velocity.x = 0;
		velocity.y = 0;
		
		float speed = context.getTarget().getSpeed();
		
		if(context.getTarget().getTrigger("Up")){
			velocity.y = speed * dt;
		}else if(context.getTarget().getTrigger("Down")) {
			velocity.y = -speed * dt;
		}
		
		if(context.getTarget().getTrigger("Left")){
			velocity.x = -speed * dt;
		}else if(context.getTarget().getTrigger("Right")) {
			velocity.x = speed * dt;
		}
		
		if(velocity.x == 0 && velocity.y == 0) {
			nextState = MovementStateMachine.state.Idle;
			return;
		}
		
		checkCollision();
		
		int direction = pointToTarget();
		
		if(velocity.x / Math.abs(velocity.x) != direction) {
			context.getAnimation().setReversed(true);
		}else {
			context.getAnimation().setReversed(false);
		}
		
		context.getTarget().changePosition(velocity.x, velocity.y);
		
		if(context.getTarget().getTrigger("Roll")){
			nextState = MovementStateMachine.state.Rolling;
			return;
		}
	}
}
