package com.states.movement;

import com.components.AnimationComponent;

public class IdleState extends MovementState  {
	
	public IdleState(MovementContext context, MovementStateMachine.movementStates stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {
		AnimationComponent a = context.getEntity().getComponent(AnimationComponent.class);
		if(a != null) {
			a.setCurrentAnimation("Idle");
		}
		this.nextState = this.stateKey;
	}

	@Override
	public void exit() {
	}

	@Override
	public void update(float dt) {
		boolean up = context.getEntity().getTrigger("Up");
		boolean down = context.getEntity().getTrigger("Down");
		boolean left = context.getEntity().getTrigger("Left");
		boolean right = context.getEntity().getTrigger("Right");
		
		if(up || down || left || right) {
			this.nextState = MovementStateMachine.movementStates.Running;
		}
	}
}
