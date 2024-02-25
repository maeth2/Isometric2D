package com.states.attack;

import com.entities.Entity;
import com.states.StateMachine;

public class AttackStateMachine extends StateMachine<AttackStateMachine.attackStates>{
	
	protected AttackContext context;
	
	public static enum attackStates {
		Q,
		E,
		R,
		BASIC,
		Idle
	}
	
	public AttackStateMachine(Entity entity) {
		this.context = new AttackContext(entity);
		context.getEntity().addTrigger("Q");
		context.getEntity().addTrigger("E");
		context.getEntity().addTrigger("R");
		context.getEntity().addTrigger("R_Click");
		context.getEntity().addTrigger("L_Click");
		initialiseStates();
		this.currentState = states.get(attackStates.Idle);
	}

	@Override
	public void initialiseStates() {
		states.put(attackStates.Idle, new AttackIdleState(context, attackStates.Idle));
	}

}
