package com.states.ai;

import com.entities.Entity;
import com.states.StateMachine;

public class EnemyStateMachine extends StateMachine<EnemyStateMachine.state, Entity, EnemyContext> {
	
	public static enum state {
		Roaming,
		Chase,
		Attack
	}
		
	public EnemyStateMachine(Entity entity) {
		super(state.Roaming, new EnemyContext(entity));
	}
	
	@Override
	public void initialiseStates() {	
		states.put(state.Roaming, new EnemyRoamingState(state.Roaming, getContext()));
		states.put(state.Chase, new EnemyChaseState(state.Chase, getContext()));
		states.put(state.Attack, new EnemyAttackState(state.Attack, getContext()));
	}
	
	public void setTargetEntity(Entity e) {
		if(currentState.getStateKey() == state.Chase) {
			EnemyChaseState.class.cast(currentState).reset();
		}else {
			getContext().setTargetEntity(e);
		}
	}
}
