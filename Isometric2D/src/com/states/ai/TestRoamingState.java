package com.states.ai;

import com.states.ai.TestAI.state;

public class TestRoamingState extends TestState{
	public TestRoamingState(state stateKey, TestContext context) {
		super(stateKey, context);
	}

	@Override
	public void enter() {		
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float dt) {
		System.out.println("Roaming...");
	}

}
