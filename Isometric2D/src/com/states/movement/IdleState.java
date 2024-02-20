package com.states.movement;

import com.components.AnimationComponent;

public class IdleState extends MovementState  {

	private float animationDuration = 0.5f;
	
	public IdleState(MovementContext context, MovementStateMachine.movementStates stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {
		AnimationComponent a = context.getEntity().getComponent(AnimationComponent.class);
		if(a != null) {
			a.setCurrentAnimation("Idle");
			a.setAnimationDuration(animationDuration);
		}
		this.nextState = this.stateKey;
	}

	@Override
	public void exit() {
	}

	@Override
	public void update(float dt) {
		boolean up = context.getEntity().getActions("Up");
		boolean down = context.getEntity().getActions("Down");
		boolean left = context.getEntity().getActions("Left");
		boolean right = context.getEntity().getActions("Right");
		if(up || down || left || right) {
			this.nextState = MovementStateMachine.movementStates.Running;
		}
	}

	@Override
	public MovementStateMachine.movementStates next() {
		return this.nextState;
	}
}
