package com.states.attack;

public class AttackIdleState extends AttackState{
	public AttackIdleState(AttackContext context, AttackStateMachine.attackStates stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {		
		this.nextState = stateKey;
	}

	@Override
	public void exit() {		
	}

	@Override
	public void update(float dt) {		
	}

}
