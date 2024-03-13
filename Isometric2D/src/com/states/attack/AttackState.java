package com.states.attack;

import com.states.State;

public abstract class AttackState extends State<AttackStateMachine.state> {
	protected AttackContext context;
	
	public AttackState(AttackContext context, AttackStateMachine.state stateKey) {
		super(stateKey);
		this.context = context;
	}
	
	public AttackState create(AttackContext context, AttackStateMachine.state stateKey) {
		return null;
	}
}
