package com.states.attack;

import com.entities.Entity;
import com.states.StateMachine;

public class AttackStateMachine extends StateMachine<AttackStateMachine.state, Entity, AttackContext>{
	
	private AttackState Q;
	private AttackState E;
	private AttackState R;
	
	public static enum state {
		Q,
		E,
		R,
		Basic,
		Idle
	}
	
	public AttackStateMachine(Entity entity, AttackState Q, AttackState E, AttackState R) {
		super(state.Idle, new AttackContext(entity));
		getContext().getTarget().addTrigger("Q");
		getContext().getTarget().addTrigger("E");
		getContext().getTarget().addTrigger("R");
		getContext().getTarget().addTrigger("R_Click");
		getContext().getTarget().addTrigger("L_Click");
		this.Q = Q;
		this.E = E;
		this.R = R;
	}

	@Override
	public void initialiseStates() {
		states.put(state.Idle, new AttackIdleState(getContext(), state.Idle));
		states.put(state.Basic, new AttackBasicState(getContext(), state.Basic));
		if(Q != null) states.put(state.Q, Q.create(getContext(), state.Q));
		if(E != null) states.put(state.E, E.create(getContext(), state.E));
		if(R != null) states.put(state.R, R.create(getContext(), state.R));
	}
	
	public void setStates(AttackStateMachine.state state, AttackState ability) {
		states.put(state, ability.create(getContext(), state));
	}
}
