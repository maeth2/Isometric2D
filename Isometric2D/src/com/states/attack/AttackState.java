package com.states.attack;

import com.states.State;
import com.states.attack.AttackStateMachine.attackStates;

public abstract class AttackState extends State<AttackStateMachine.attackStates> {

	protected AttackContext context;
	
	public AttackState(AttackContext context, attackStates stateKey) {
		super(stateKey);
		this.context = context;
	}

}
