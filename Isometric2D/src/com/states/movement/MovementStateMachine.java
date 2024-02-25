package com.states.movement;

import com.entities.Entity;
import com.states.StateMachine;

public class MovementStateMachine extends StateMachine<MovementStateMachine.movementStates>{
	
	protected MovementContext context;
	
	public static enum movementStates {
		Running,
		Rolling,
		Idle
	}
	
	public MovementStateMachine(Entity entity) {
		this.context = new MovementContext(entity);
		context.getEntity().addTrigger("Left");
		context.getEntity().addTrigger("Right");
		context.getEntity().addTrigger("Up");
		context.getEntity().addTrigger("Down");
		context.getEntity().addTrigger("Roll");
		context.getEntity().addTrigger("Stun");
		initialiseStates();
		this.currentState = states.get(movementStates.Idle);
	}

	@Override
	public void initialiseStates() {
		states.put(movementStates.Running, new RunningState(context, movementStates.Running));
		states.put(movementStates.Rolling, new RollingState(context, movementStates.Rolling));
		states.put(movementStates.Idle, new IdleState(context, movementStates.Idle));
	}
}
