package com.states.ai;

import com.entities.Entity;
import com.states.StateMachine;

public class TestAI extends StateMachine<TestAI.state, Entity, TestContext> {
	
	public static enum state {
		Roaming
	}
	
	protected TestContext context;
	
	public TestAI(Entity entity) {
		super(state.Roaming, new TestContext(entity));
	}
	
	@Override
	public void initialiseStates() {	
		states.put(state.Roaming, new TestRoamingState(state.Roaming, context));
	}
}
