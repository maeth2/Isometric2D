package com.states.ai;

import com.states.State;

public abstract class TestState extends State<TestAI.state> {
	protected TestContext context;
	
	public TestState(TestAI.state stateKey, TestContext context) {
		super(stateKey);
		this.context = context;
	}
}
