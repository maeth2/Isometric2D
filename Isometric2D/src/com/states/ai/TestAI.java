package com.states.ai;

import com.entities.Entity;
import com.states.StateMachine;

public class TestAI extends StateMachine<TestAI.state, Entity, TestContext> {
	
	public static enum state {
		Roaming,
		Chase
	}
		
	public TestAI(Entity entity) {
		super(state.Roaming, new TestContext(entity));
	}
	
	@Override
	public void initialiseStates() {	
		states.put(state.Roaming, new TestRoamingState(state.Roaming, getContext()));
		states.put(state.Chase, new TestChaseState(state.Chase, getContext()));
	}
	
	public void setTargetEntity(Entity e) {
		getContext().setTargetEntity(e);
	}
}
