package com.states.movement;

import com.entities.Entity;
import com.states.StateMachine;

public class MovementStateMachine extends StateMachine<MovementStateMachine.state, Entity, MovementContext>{	
	public static enum state {
		Running,
		Rolling,
		Idle,
		Knockback
	}
	
	public MovementStateMachine(Entity entity) {
		super(state.Idle, new MovementContext(entity));
		getContext().getTarget().addTrigger("Left");
		getContext().getTarget().addTrigger("Right");
		getContext().getTarget().addTrigger("Up");
		getContext().getTarget().addTrigger("Down");
		getContext().getTarget().addTrigger("Roll");
		initialiseStates();
		this.currentState = states.get(state.Idle);
	}

	@Override
	public void initialiseStates() {
		states.put(state.Running, new RunningState(getContext(), state.Running));
		states.put(state.Rolling, new RollingState(getContext(), state.Rolling));
		states.put(state.Idle, new IdleState(getContext(), state.Idle));
		states.put(state.Knockback, new KnockbackState(getContext(), state.Knockback));
	}
	
	/**
	 * Knockback Trigger
	 * 
	 * @param distance			Knockback distance
	 */
	public void onKnockback(float dx, float dy) {
		if(currentState.getStateKey() != state.Knockback) {
			getContext().setKnockbackDistance(dx, dy);
			setState(state.Knockback);
		}
	}
}
