package com.states.movement;

import com.entities.Entity;

public class IdleState extends MovementState  {
	
	public IdleState(MovementContext context, MovementStateMachine.state stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {
		if(context.getAnimation() != null) {
			context.getAnimation().setCurrentAnimation(Entity.states.Idle);
		}
	}

	@Override
	public void exit() {
	}

	@Override
	public void update(float dt) {		
		boolean up = context.getTarget().getTrigger("Up");
		boolean down = context.getTarget().getTrigger("Down");
		boolean left = context.getTarget().getTrigger("Left");
		boolean right = context.getTarget().getTrigger("Right");
		
		if(up || down || left || right) {
			this.nextState = MovementStateMachine.state.Running;
		}
		
		pointToTarget();
	}
}
